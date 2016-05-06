package com.xidian.yetwish.reading.ui.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.common.eventbus.Subscribe;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedChapter;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedPage;
import com.xidian.yetwish.reading.framework.reader.ChapterFactory;
import com.xidian.yetwish.reading.framework.reader.PageFactory;
import com.xidian.yetwish.reading.framework.utils.BookUtils;
import com.xidian.yetwish.reading.framework.utils.Constant;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;
import com.xidian.yetwish.reading.ui.main.adapter.ChapterAdapter;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.PageChangedListenerImpl;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.ReaderPageAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.DepthPageTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * todo 正在加载 请稍后
 * 文本阅读activity  根据手机屏幕计算出rows and cols
 * Created by Yetwish on 2016/4/23 0023.
 */
public class ReaderActivity extends Activity implements View.OnClickListener {

    public static final void startActivity(Context context, String bookPath) {
        Intent intent = new Intent(context, ReaderActivity.class);
        intent.putExtra(Constant.EXTRA_FILE_PATH, bookPath);
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


    private List<PageVo> mPageList = new ArrayList<>();
    private List<ChapterVo> mChapters = new ArrayList<>();
    private CommonAdapter<ChapterVo> mChapterAdapter;
    private ReaderPageAdapter mPageAdapter;

    /**
     * menuBar view
     */
    private View mTopContainer;
    private View mBottomContainer;
    private View mMidContainer;
    //todo
    private NumberProgressBar pbProgress;
    private int topBarHeight;
    private int bottomBarHeight;

    private boolean isHiding = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusWrapper.getDefault().register(this);
        setContentView(R.layout.activity_reader);
        initView();
        initData();
    }

    private void initData() {
        try {
            BookVo book = new BookVo("大荒蛮神", "更俗", 0.1f, 0);
            book.setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/QQfile_recv/大荒蛮神.txt");
//            book.setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/test.txt");
//            book.setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/UCDownloads/test2.txt");
            book.setBookId(BookUtils.generateSequenceId());
            ChapterFactory.getsInstance().concurrentReadFile(book);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        initMenuBar();
        initSlideMenu();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mViewPager = (ViewPager) findViewById(R.id.vpReaderContainer);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mPageAdapter = new ReaderPageAdapter(ReaderActivity.this, mPageList);
        mPageAdapter.setOnClickListener(new ReaderPageAdapter.OnReaderViewClickListener() {
            @Override
            public void onClick() {
                if (mTopContainer.isShown()) {
                    LogUtils.w("hide");
                    hideMenu();
                } else {
                    LogUtils.w("show");
                    showMenu();
                }
            }
        });
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(new PageChangedListenerImpl());

//        mReaderView = (ReaderView) findViewById(R.id.mReaderView);

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
        mChapterAdapter = new ChapterAdapter(ReaderActivity.this, mChapters);
        lvChapter.setAdapter(mChapterAdapter);

        rgTabs = (RadioGroup) findViewById(R.id.rgTabs);
        rbChapter = (RadioButton) rgTabs.findViewById(R.id.rbChapter);
        rbBookMark = (RadioButton) rgTabs.findViewById(R.id.rbBookMark);

        rgTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivReaderMenu:
                hideMenu();
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ivReaderBack:
                //返回
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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe
    public void onChapterGenerated(EventGeneratedChapter event) {
        mChapters.clear();
        mChapters.addAll(event.getChapterList());
        mChapterAdapter.notifyDataSetChanged();
        //设置初始 page 数据
        PageFactory.getInstance(ReaderActivity.this).paging(mChapters.get(0));
        PageFactory.getInstance(ReaderActivity.this).paging(mChapters.get(1));

    }


    @Subscribe
    public void onPageGenerator(EventGeneratedPage event) {
        //todo  存在一个问题， 选择某一章时，获取到的分页列表 、 判断是否已经存在数据 应该放在哪个地方？
        mPageList.addAll(event.getPageList());
        mPageAdapter.notifyDataSetChanged();
    }


}
