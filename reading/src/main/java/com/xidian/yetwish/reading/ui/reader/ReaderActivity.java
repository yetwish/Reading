package com.xidian.yetwish.reading.ui.reader;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedChapter;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedPage;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGetBookmarkList;
import com.xidian.yetwish.reading.framework.exception.IllegalIntentDataException;
import com.xidian.yetwish.reading.framework.reader.ChapterFactory;
import com.xidian.yetwish.reading.framework.reader.PageFactory;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.utils.SharedPreferencesUtils;
import com.xidian.yetwish.reading.framework.utils.ToastUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.NoteBookVo;
import com.xidian.yetwish.reading.framework.vo.reader.BookmarkVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;
import com.xidian.yetwish.reading.ui.note.NoteEditActivity;
import com.xidian.yetwish.reading.ui.reader.adapter.BookmarkAdapter;
import com.xidian.yetwish.reading.ui.reader.adapter.ChapterAdapter;
import com.xidian.yetwish.reading.ui.reader.viewpager.DepthPageTransformer;
import com.xidian.yetwish.reading.ui.reader.viewpager.ReaderPageAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * todo 更换readerView实现方式，提供seekBar
 * 文本阅读activity  根据手机屏幕计算出rows and cols
 * Created by Yetwish on 2016/4/23 0023.
 */
public class ReaderActivity extends Activity implements View.OnClickListener, IReaderProgressChangeListener {

    public static void startActivity(Context context, BookVo book) {
        Intent intent = new Intent(context, ReaderActivity.class);
        intent.putExtra(SharedPreferencesUtils.EXTRA_BOOK, book);
        context.startActivity(intent);
    }

    /**
     * 当前展示出来的章节 下标
     */
    private int mCurChapter = 0;
    /**
     * 当前展示出来的页面的下标
     */
    private int mCurPageIndex = -1;

    /**
     * 当前阅读的书本vo
     */
    private BookVo mBook;

    /**
     * 进入该页面应滚动到的初始位置，由book progress获得
     * 保存已读当前页面的最后一个字符的位置
     */
    private long mPosition = 0;

    /**
     * 标记当前page有无bookmark 从而设置bookmark的图标
     */
    private boolean hasMark = false;
    /**
     * 保存当前已加载（到内存）的章节的下标集
     */
    private List<Integer> loadedChapters;

    /**
     * 当前正在加载的章节 下标
     */
    private int loadingChapterIndex;

    /**
     * menuBar view
     */
    private boolean isHiding = false;

    private View mTopContainer;
    private View mBottomContainer;
    private View mMidContainer;

    private ImageView ivBookMark;
    private View bookmarkView;

    //    private SeekBar mSeekBar;
    private ProgressBar mProgressBar;
    private TextView tvName;

    private int topBarHeight;
    private int bottomBarHeight;


    /**
     * viewpager and slide menu
     */
    private DrawerLayout mDrawerLayout;
    private EmptyRecyclerView lvChapter;
    private ViewPager mViewPager;
    private RadioGroup rgTabs;


    private View mEmptyView;

    private List<PageVo> mPageList = new ArrayList<>();
    private ReaderPageAdapter mPageAdapter;
    private ReaderPageAdapter mPageAdapter2;

    private List<ChapterVo> mChapterList;
    private CommonAdapter<ChapterVo> mChapterAdapter;

    private List<BookmarkVo> mBookmarkList;
    private CommonAdapter<BookmarkVo> mBookMarkAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusWrapper.getDefault().register(this);
        setContentView(R.layout.activity_reader);
        loadedChapters = new ArrayList<>();
        initView();
        initData();
    }

    /**
     * 初始化数据，获取章节列表，再加载页面信息
     */
    private void initData() {
        Serializable data = getIntent().getSerializableExtra(SharedPreferencesUtils.EXTRA_BOOK);
        if (!(data instanceof BookVo))
            throw new IllegalIntentDataException(BookVo.class, data.getClass());
        mBook = (BookVo) data;
        tvName.setText(mBook.getName());
        //判断数据库中是否存储该book的章节信息，若无则重新扫描一遍书本
        DatabaseManager.getsInstance().getChapterManager().queryByBookId(mBook.getBookId(), new ApiCallback<ImmutableList<ChapterVo>>() {
            @Override
            public void onDataReceived(ImmutableList<ChapterVo> chapterList) {
                if (chapterList.size() != 0) {
                    mChapterList.addAll(chapterList);
                    if (mChapterAdapter != null)
                        mChapterAdapter.notifyDataSetChanged();
                    mPosition = Math.round(mBook.getCharNumber() * mBook.getProgress() / 100);
//                    LogUtils.w(mPosition+" position");
                    ChapterVo chapter = DatabaseManager.getsInstance().getChapterManager()
                            .queryByPosition(mPosition);
                    if (chapter == null) {
                        loadChapter(0, true);
                        return;
                    }

                    int chapterIndex = -1;
                    for (int i = 0; i < mChapterList.size(); i++) {
                        if (mChapterList.get(i).getChapterId() == chapter.getChapterId()) {
                            chapterIndex = i;
                            break;
                        }
                    }
                    if (chapterIndex < 0)
                        chapterIndex = 0;
                    loadChapter(chapterIndex, true);//加载当前章节
                    mCurChapter = chapterIndex;
                    loadPreChapter();//加载前一章
                    //获取书签列表
                    DatabaseManager.getsInstance().getBookmarkManager().AsyncQuery(mBook.getBookId());
                } else {
                    try {
                        ChapterFactory.createDivider().scanBook(mBook);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onException(int code, String reason) {

            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initMenuBar();
        initSlideMenu();
        mEmptyView = findViewById(R.id.readerEmptyView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mViewPager = (ViewPager) findViewById(R.id.vpReaderContainer);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mPageAdapter = new ReaderPageAdapter(ReaderActivity.this, mPageList);
        mPageAdapter.setOnClickListener(mReaderClickListener);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);

//        关闭滑动手势
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    /**
     * 初始化滑动菜单
     */
    private void initSlideMenu() {

        lvChapter = (EmptyRecyclerView) findViewById(R.id.lvChapter);

        mChapterList = new ArrayList<>();
        mBookmarkList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(ReaderActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvChapter.setLayoutManager(layoutManager);
        mChapterAdapter = new ChapterAdapter(ReaderActivity.this, mChapterList, this);
        mBookMarkAdapter = new BookmarkAdapter(ReaderActivity.this, mBookmarkList, this);
        //默认展示章节列表
        lvChapter.setAdapter(mChapterAdapter);

        rgTabs = (RadioGroup) findViewById(R.id.rgTabs);
        rgTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbChapter)
                    lvChapter.setAdapter(mChapterAdapter);
                else if (checkedId == R.id.rbBookmark) {
                    lvChapter.setAdapter(mBookMarkAdapter);
                }
            }
        });
    }

    /**
     * 初始化菜单栏视图
     */
    private void initMenuBar() {
        mTopContainer = findViewById(R.id.rlReaderTopContainer);
        mBottomContainer = findViewById(R.id.llReaderBottomContainer);
        mMidContainer = findViewById(R.id.midContainer);

        mTopContainer.setOnClickListener(this);
        mMidContainer.setOnClickListener(this);
        mBottomContainer.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.readerProgress);

        findViewById(R.id.ivReaderMenu).setOnClickListener(this);
        findViewById(R.id.ivReaderBack).setOnClickListener(this);
        findViewById(R.id.tvPreChapter).setOnClickListener(this);
        findViewById(R.id.tvNextChapter).setOnClickListener(this);
        findViewById(R.id.ivReaderTextSize).setOnClickListener(this);
        findViewById(R.id.ivReaderNote).setOnClickListener(this);

        ivBookMark = (ImageView) findViewById(R.id.ivReaderBookMark);
        ivBookMark.setOnClickListener(this);

        bookmarkView = findViewById(R.id.ivBookmarkIcon);

        tvName = (TextView) findViewById(R.id.tvReaderName);

        topBarHeight = getResources().getDimensionPixelOffset(R.dimen.reader_popup_top_height);
        bottomBarHeight = getResources().getDimensionPixelOffset(R.dimen.reader_popup_bottom_height);
    }

    /**
     * 监听ReaderView的点击事件，控制弹出/关闭菜单。
     */
    private ReaderPageAdapter.OnReaderViewClickListener mReaderClickListener = new ReaderPageAdapter.OnReaderViewClickListener() {
        @Override
        public void onClick() {
            if (mTopContainer.isShown()) {
                hideMenu();
            } else {
                showMenu();
            }
        }
    };

    /**
     * 监听viewPager的滑动，更新readerView的数据
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //更新curIndex 判断是否加载了前/后chapter无，则加载
            mCurPageIndex = position;
            mCurChapter = getChapterIndex();
            updateProgress();
            loadPreChapter();
            loadNextChapter();
            if (mBookmarkList != null && mBookmarkList.size() > 0) {
                int index = getBookmarkIndex();
                if (index != -1)
                    hasMark = true;
                else
                    hasMark = false;
                updateBookmarkView();

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 判断当前页是否有书签
     *
     * @return
     */
    private int getBookmarkIndex() {
        long lastCharPosition = mPageList.get(mCurPageIndex).getLastCharPosition();
        int index = -1;
        for (int i = 0; i < mBookmarkList.size(); i++) {
            if (mBookmarkList.get(i).getLastCharPosition() == lastCharPosition)
                index = i;
        }
        return index;
    }

    /**
     * 更新书签菜单的视图
     */
    private void updateBookmarkView() {
        if (ivBookMark == null) return;
        if (hasMark) {
            ivBookMark.setImageResource(R.mipmap.ic_bookmark_select_36dp);
            bookmarkView.setVisibility(View.VISIBLE);
        } else {
            ivBookMark.setImageResource(R.mipmap.ic_bookmark_white_36dp);
            bookmarkView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取当前chapter index
     *
     * @return
     */
    private synchronized int getChapterIndex() {
        int index = 0;
        long position = mPageList.get(mCurPageIndex).getFirstCharPosition();
        for (int i = 0; i < mChapterList.size(); i++) {
            if (mChapterList.get(i).getFirstCharPosition() <= position &&
                    mChapterList.get(i).getLastCharPosition() > position) {
                index = i;
            }
        }
        return index;
    }

    /**
     * 根据第一个字符的position获取想应的页面
     *
     * @param firstCharIndex
     * @return
     */
    private synchronized int getPageIndex(long firstCharIndex) {
        if (mPageList.size() == 0) return 0;
        int index = mPageList.size();
        for (int i = 0; i < mPageList.size(); i++) {
            if (firstCharIndex <= mPageList.get(i).getFirstCharPosition()) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 根据最后一个字符的position获取想应的页面
     *
     * @param lastCharIndex
     * @return
     */
    private synchronized int getPageIndexByLastIndex(long lastCharIndex) {
        if (mPageList.size() == 0) return 0;
        int index = mPageList.size();
        for (int i = 0; i < mPageList.size(); i++) {
            if (lastCharIndex <= mPageList.get(i).getLastCharPosition()) {
                index = i;
                break;
            }
        }
        return index;
    }


    private void loadChapter(final int index, boolean progressChange) {
        if (mChapterList.size() == 0)
            return;
        //判断内存中是否已加载
        if (progressChange && loadedChapters.contains(index)) {
            //跳转到该index
            int pageIndex;
            if (mPosition != 0) {
                pageIndex = getPageIndexByLastIndex(mPosition);
                mPosition = 0;
                mViewPager.setCurrentItem(pageIndex, true);
            } else {
                pageIndex = getPageIndex(mChapterList.get(index).getFirstCharPosition());
                mViewPager.setCurrentItem(pageIndex, false);
            }
            mCurPageIndex = pageIndex;
            updateProgress();
            return;
        }
        if (loadedChapters.contains(index))
            return;
        if (progressChange) {
            loadingChapterIndex = index;
            mEmptyView.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
        }
        DatabaseManager.getsInstance().getPageManager().queryByChapter(mChapterList.get(index).getChapterId(), new ApiCallback<ImmutableList<PageVo>>() {
            @Override
            public void onDataReceived(ImmutableList<PageVo> pageList) {
                if (pageList.size() != 0) {
                    addLoadedPageList(index, pageList);
                } else {
                    PageFactory.createPageDivider(ReaderActivity.this).paging(mChapterList.get(index));
                }
            }

            @Override
            public void onException(int code, String reason) {

            }
        });


    }

    private void loadNextChapter() {
        if (mCurChapter == mChapterList.size() - 1) {
//            if (progressChange)
//                ToastUtils.showShort(ReaderActivity.this, getString(R.string.no_more_next_chapter));
            return;
        }
        loadChapter(mCurChapter + 1, false);
    }

    private void loadPreChapter() {
        if (mCurChapter == 0) {
//                ToastUtils.showShort(ReaderActivity.this, getString(R.string.no_more_pre_chapter));
            return;
        }
        loadChapter(mCurChapter - 1, false);
    }

    @Override
    public void onProgressChanged(int chapterIndex, long charPosition, boolean withPosition) {
        if (chapterIndex >= mChapterList.size() || chapterIndex < 0) return;
        if (withPosition) mPosition = charPosition;
        loadChapter(chapterIndex, true);
        mCurChapter = chapterIndex;
        loadPreChapter();
        updateProgress();
        closeDrawer();
    }


    private void updateProgress() {
        float progress = mPageList.get(mCurPageIndex).getLastCharPosition() * 100.0f / mBook.getCharNumber();
        mProgressBar.setProgress((int) Math.floor(progress * 100));
        mBook.setProgress(progress);
    }

    private synchronized void addLoadedPageList(int chapterIndex, ImmutableList<PageVo> list) {
        if (list == null || list.size() == 0) return;
        loadedChapters.add(chapterIndex);
        int pageIndex = getPageIndex(list.get(0).getFirstCharPosition());

        if (pageIndex <= mCurPageIndex) {
            mCurPageIndex += list.size();
            mPageList.addAll(pageIndex, list);
            if (mViewPager.getAdapter().equals(mPageAdapter)) {
                mPageAdapter2 = new ReaderPageAdapter(ReaderActivity.this, mPageList);
                mPageAdapter2.setOnClickListener(mReaderClickListener);
                mViewPager.setAdapter(mPageAdapter2);
                mPageAdapter = null;
            } else {
                mPageAdapter = new ReaderPageAdapter(ReaderActivity.this, mPageList);
                mViewPager.setAdapter(mPageAdapter);
                mPageAdapter.setOnClickListener(mReaderClickListener);
                mPageAdapter2 = null;
            }
            mViewPager.setCurrentItem(mCurPageIndex, false);
        } else {
            ((ReaderPageAdapter) mViewPager.getAdapter()).addPages(pageIndex, list);
        }
        if (chapterIndex == loadingChapterIndex) {
            mViewPager.setVisibility(View.VISIBLE);
            if (mPosition != 0) {
                pageIndex = getPageIndexByLastIndex(mPosition);
                mViewPager.setCurrentItem(pageIndex, true);
                mPosition = 0;
            } else {
                pageIndex = getPageIndex(list.get(0).getFirstCharPosition());
                mViewPager.setCurrentItem(pageIndex, true);
            }
            mCurPageIndex = pageIndex;
            mCurChapter = chapterIndex;
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onChapterGenerated(EventGeneratedChapter event) {
        mChapterList.clear();
        mChapterList.addAll(event.getChapterList());
        mChapterAdapter.notifyDataSetChanged();
        //设置初始 page 数据  //0->progress
        loadChapter(0, true);
    }


    @Subscribe
    public void onPageGenerator(EventGeneratedPage event) {
        int chapterIndex = mChapterList.indexOf(event.getChapter());
        addLoadedPageList(chapterIndex, event.getPageList());
    }


    @Subscribe
    public void onGetBookmarkList(EventGetBookmarkList event) {
        if (event.getBookmarkList() == null) return;
        //获取bookmarkList
        mBookmarkList.clear();
        mBookmarkList.addAll(event.getBookmarkList());
        mBookMarkAdapter.notifyDataSetChanged();

    }

    /**
     * 弹出菜单
     */
    private void showMenu() {
        if (mTopContainer.isShown()) return;
        mTopContainer.setVisibility(View.VISIBLE);
        mBottomContainer.setVisibility(View.VISIBLE);
        mMidContainer.setVisibility(View.VISIBLE);

        ObjectAnimator topEnterAni = new ObjectAnimator().ofFloat(mTopContainer, "y", -topBarHeight, 0);
        topEnterAni.setDuration(200);

        ObjectAnimator bottomEnterAni = new ObjectAnimator().ofFloat(mBottomContainer,
                "y", ScreenUtils.getScreenHeight(ReaderActivity.this), ScreenUtils.getScreenHeight(ReaderActivity.this) - bottomBarHeight);
        bottomEnterAni.setDuration(200);

        topEnterAni.start();
        bottomEnterAni.start();
    }

    /**
     * 隐藏菜单
     */
    private void hideMenu() {
        if (isHiding) return;
        ObjectAnimator topExitAni = new ObjectAnimator().ofFloat(mTopContainer, "y", 0, -topBarHeight);
        topExitAni.setDuration(150);

        ObjectAnimator bottomExitAni = new ObjectAnimator().ofFloat(mBottomContainer,
                "y", ScreenUtils.getScreenHeight(ReaderActivity.this) - bottomBarHeight, ScreenUtils.getScreenHeight(ReaderActivity.this));
        bottomExitAni.setDuration(150);

        topExitAni.addListener(mAnimationListener);
        bottomExitAni.addListener(mAnimationListener);

        topExitAni.start();
        bottomExitAni.start();
    }

    /**
     * 监听菜单弹出/隐藏的动画
     */
    private Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isHiding = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mTopContainer.setVisibility(View.GONE);
            mBottomContainer.setVisibility(View.GONE);
            mMidContainer.setVisibility(View.GONE);
            isHiding = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };


    private boolean closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivReaderMenu:
                hideMenu();
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ivReaderBack:
                //返回
                finish();
                break;
            case R.id.tvPreChapter:
                //上一章
//                loadPreChapter(true);
                break;
            case R.id.tvNextChapter:
                //下一章
//                loadNextChapter(true);
                break;
            case R.id.ivReaderTextSize:
                //字体大小
                break;
            case R.id.ivReaderBookMark:
                //书签
                if (hasMark) { //取消
                    hasMark = false;
                    BookmarkVo bookmark = mBookmarkList.get(getBookmarkIndex());
                    DatabaseManager.getsInstance().getBookmarkManager().delete(bookmark.getBookmarkId());
                    mBookmarkList.remove(bookmark);
                    mBookMarkAdapter.notifyDataSetChanged();
                    ToastUtils.showShort(ReaderActivity.this, "已删除该书签");
                } else {//添加
                    hasMark = true;
                    BookmarkVo bookmark = new BookmarkVo(mBook.getBookId(), mPageList.get(mCurPageIndex).getLastCharPosition(),
                            mChapterList.get(mCurChapter).getName(), mCurChapter);
                    DatabaseManager.getsInstance().getBookmarkManager().refresh(bookmark);
                    mBookmarkList.add(bookmark);
                    mBookMarkAdapter.notifyDataSetChanged();
                    ToastUtils.showShort(ReaderActivity.this, "已添加书签！可在书签列表中查看");
                }
                updateBookmarkView();
                break;
            case R.id.ivReaderNote:
                //笔记
                //判断是否有该笔记本
                NoteBookVo noteBook = DatabaseManager.getsInstance().getNoteBookManager()
                        .queryNoteBookByBook(mBook.getBookId());
                if (noteBook == null) {
                    noteBook = new NoteBookVo(mBook.getName());
                    noteBook.setBookId(mBook.getBookId());
                    DatabaseManager.getsInstance().getNoteBookManager().refresh(noteBook);
                }
                NoteEditActivity.startActivity(ReaderActivity.this, noteBook.getNoteBookId());
                break;
            case R.id.midContainer:
                hideMenu();
                break;
            case R.id.rlReaderTopContainer:
            case R.id.llReaderBottomContainer:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save progress
        DatabaseManager.getsInstance().getBookManager().refresh(mBook);
//        LogUtils.w("pause " + mPageList.get(mCurPageIndex).getFirstCharPosition());
//        LogUtils.w("pause " + mChapterList.get(mCurChapter));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChapterList.clear();
        loadedChapters.clear();

    }

    @Override
    public void onBackPressed() {
        if (closeDrawer()) {
            return;
        }
        super.onBackPressed();
    }


}
