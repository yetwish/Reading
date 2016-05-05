package com.xidian.yetwish.reading.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;
import com.xidian.yetwish.reading.ui.main.adapter.ChapterAdapter;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.ReaderPageAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.DepthPageTransformer;
import com.xidian.yetwish.reading.ui.widget.PopupReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * todo 正在加载 请稍后
 * 文本阅读activity  根据手机屏幕计算出rows and cols
 * Created by Yetwish on 2016/4/23 0023.
 */
public class ReaderActivity extends Activity {

    public static final void startActivity(Context context, String bookPath) {
        Intent intent = new Intent(context, ReaderActivity.class);
        intent.putExtra(Constant.EXTRA_FILE_PATH, bookPath);
        context.startActivity(intent);
    }

    private DrawerLayout mDrawerLayout;
    private EmptyRecyclerView lvChapter;

    private List<PageVo> mPageList = new ArrayList<>();

    private ViewPager mViewPager;

    private List<ChapterVo> mChapters = new ArrayList<>();

    private CommonAdapter<ChapterVo> mChapterAdapter;

    private ReaderPageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusWrapper.getDefault().register(this);
        setContentView(R.layout.activity_reader);
//        for (int i = 0; i < 10; i++) {
//            mPageList.add(new ReaderView(ReaderActivity.this));
//        }
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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        lvChapter = (EmptyRecyclerView) findViewById(R.id.lvChapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ReaderActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvChapter.setLayoutManager(layoutManager);
        mChapterAdapter = new ChapterAdapter(ReaderActivity.this, mChapters);
        lvChapter.setAdapter(mChapterAdapter);

        mViewPager = (ViewPager) findViewById(R.id.vpReaderContainer);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mPageAdapter = new ReaderPageAdapter(ReaderActivity.this, mPageList, new ReaderPageAdapter.OnClickListener() {
            @Override
            public void onClick() {
                //todo onclick
                PopupReader popupReader = new PopupReader(ReaderActivity.this);
                popupReader.show(mViewPager);
            }
        });
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(mPageChangedListener);

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

    private ViewPager.OnPageChangeListener mPageChangedListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };





    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(lvChapter)) {
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

    }


    @Subscribe
    public void onPageGenerator(EventGeneratedPage event) {
        //todo  存在一个问题， 选择某一章时，获取到的分页列表 、 判断是否已经存在数据 应该放在哪个地方？
        mPageList.addAll(event.getPageList());
        mPageAdapter.notifyDataSetChanged();
    }

}
