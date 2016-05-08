package com.xidian.yetwish.reading.ui.main;

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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedChapter;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedPage;
import com.xidian.yetwish.reading.framework.reader.ChapterFactory;
import com.xidian.yetwish.reading.framework.reader.PageFactory;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.utils.Constant;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;
import com.xidian.yetwish.reading.ui.main.adapter.ChapterAdapter;
import com.xidian.yetwish.reading.ui.main.adapter.IReaderProgressChangeListener;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.DepthPageTransformer;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.ReaderPageAdapter;
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
        intent.putExtra(Constant.EXTRA_BOOK, book);
        context.startActivity(intent);
    }

    /**
     * viewpager and slide menu
     */
    private DrawerLayout mDrawerLayout;
    private EmptyRecyclerView lvChapter;
    private ViewPager mViewPager;
    private RadioGroup rgTabs;
    private RadioButton rbChapter;
    private RadioButton rbBookMark;


    private View mEmptyView;

    private List<PageVo> mPageList = new ArrayList<>();
    private List<ChapterVo> mChapterList = new ArrayList<>();
    private CommonAdapter<ChapterVo> mChapterAdapter;
    private ReaderPageAdapter mPageAdapter;
    private ReaderPageAdapter mPageAdapter2;

    private int mCurChapter = 0;
    private int mCurPageIndex = -1;

    private List<Integer> loadedIndexs;

    private int loadingIndex;

    /**
     * menuBar view
     */
    private View mTopContainer;
    private View mBottomContainer;
    private View mMidContainer;

    //todo
//    private SeekBar mSeekBar;
    private ProgressBar mProgressBar;

    private int topBarHeight;
    private int bottomBarHeight;

    private boolean isHiding = false;

    private BookVo mBook;

    private long mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusWrapper.getDefault().register(this);
        setContentView(R.layout.activity_reader);
        loadedIndexs = new ArrayList<>();
        initView();
        initData();
    }

    private void initData() {
        Serializable obj = getIntent().getSerializableExtra(Constant.EXTRA_BOOK);
        if (!(obj instanceof BookVo))
            return;
        mBook = (BookVo) obj;
        //判断该book是否生成过章节目录，如果生成过，则从数据库中获取
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
                    int chapterIndex = -1;
                    for (int i = 0; i < mChapterList.size(); i++) {
                        if (mChapterList.get(i).getChapterId() == chapter.getChapterId()) {
                            chapterIndex = i;
                            break;
                        }

                    }
                    if (chapterIndex < 0)
                        chapterIndex = 0;
                    loadChapter(chapterIndex, true);
                } else {
                    try {
                        ChapterFactory.getsInstance().concurrentReadFile(mBook);
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

    private void initSlideMenu() {

        lvChapter = (EmptyRecyclerView) findViewById(R.id.lvChapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ReaderActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvChapter.setLayoutManager(layoutManager);
        mChapterAdapter = new ChapterAdapter(ReaderActivity.this, mChapterList, this);
        lvChapter.setAdapter(mChapterAdapter);

        rgTabs = (RadioGroup) findViewById(R.id.rgTabs);
        rbChapter = (RadioButton) rgTabs.findViewById(R.id.rbChapter);
        rbBookMark = (RadioButton) rgTabs.findViewById(R.id.rbBookMark);

        rgTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //todo
            }
        });

    }

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
        findViewById(R.id.ivReaderBookMark).setOnClickListener(this);
        findViewById(R.id.ivReaderNote).setOnClickListener(this);

        topBarHeight = getResources().getDimensionPixelOffset(R.dimen.reader_popup_top_height);
        bottomBarHeight = getResources().getDimensionPixelOffset(R.dimen.reader_popup_bottom_height);
    }


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

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //更新curIndex 判断是否加载了前/后chapter无，则加载
            mCurPageIndex = position;
            mCurChapter = getChapterIndex(position);
            updateProgress();
            loadPreChapter();
            loadNextChapter();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 获取当前chapter index
     *
     * @param position
     * @return
     */
    private synchronized int getChapterIndex(int position) {
        int index = 0;
        for (int i = 0; i < mChapterList.size() && position < mPageList.size(); i++) {
            if (mChapterList.get(i).getFirstCharPosition() < mPageList.get(position).getFirstCharPosition() &&
                    mChapterList.get(i).getLastCharPosition() > mPageList.get(position).getFirstCharPosition()) {
                index = i;
            }
        }
        return index;
    }


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

    private void loadChapter(final int index, boolean progressChange) {
        if (mChapterList.size() == 0)
            return;
        //判断内存中是否已加载
        if (progressChange && loadedIndexs.contains(index)) {
            //跳转到该index
            int pageIndex = getPageIndex(mChapterList.get(index).getFirstCharPosition());
            mViewPager.setCurrentItem(pageIndex, false);
            mCurPageIndex = pageIndex;
            updateProgress();
            return;
        }
        if (progressChange) {
            loadingIndex = index;
            mEmptyView.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
        }
        DatabaseManager.getsInstance().getPageManager().queryByChapter(mChapterList.get(index).getChapterId(), new ApiCallback<ImmutableList<PageVo>>() {
            @Override
            public void onDataReceived(ImmutableList<PageVo> pageList) {
                if (pageList.size() != 0) {
                    addLoadedPageList(index, pageList);
                } else {
                    PageFactory.getInstance(ReaderActivity.this).paging(mChapterList.get(index));
                }
            }

            @Override
            public void onException(int code, String reason) {

            }
        });


    }

    private void loadNextChapter() {
        if (mCurChapter == mChapterList.size() - 1)
            return;
        if (!loadedIndexs.contains(mCurChapter + 1))
            loadChapter(mCurChapter + 1, false);
    }

    private void loadPreChapter() {
        if (mCurChapter == 0)
            return;
        if (!loadedIndexs.contains(mCurChapter - 1))
            loadChapter(mCurChapter - 1, false);
    }

    @Override
    public void onProgressChanged(int chapterIndex, long firstCharPosition) {
        if (chapterIndex >= mChapterList.size() || chapterIndex < 0) return;
        loadChapter(chapterIndex, true);
        mCurChapter = chapterIndex;
        loadPreChapter();
        updateProgress();
        closeDrawer();
    }


    private void updateProgress() {
        float progress = mPageList.get(mCurPageIndex).getFirstCharPosition() * 100.0f / mBook.getCharNumber();
        mProgressBar.setProgress((int) Math.floor(progress * 100));
        mBook.setProgress(progress);
    }

    private synchronized void addLoadedPageList(int chapterIndex, ImmutableList<PageVo> list) {
        if (list == null || list.size() == 0) return;
        loadedIndexs.add(chapterIndex);
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
            ((ReaderPageAdapter) mViewPager.getAdapter()).addPages(list);
        }
        if (chapterIndex == loadingIndex) {
            mEmptyView.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            if (mPosition != 0) {
                pageIndex = getPageIndex(mPosition);
                mPosition = 0;
            }else {
                pageIndex = getPageIndex(list.get(0).getFirstCharPosition());
            }
            mViewPager.setCurrentItem(pageIndex, false);
            mCurPageIndex = pageIndex;
            mCurChapter = chapterIndex;
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
                break;
            case R.id.tvNextChapter:
                //下一章
                break;
            case R.id.ivReaderTextSize:
                //字体大小
                break;
            case R.id.ivReaderBookMark:
                //书签
                break;
            case R.id.ivReaderNote:
                //笔记
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
        loadedIndexs.clear();
    }

    @Override
    public void onBackPressed() {
        if (closeDrawer()) {
            return;
        }
        super.onBackPressed();
    }


}
