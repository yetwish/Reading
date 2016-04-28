package com.xidian.yetwish.reading.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.reader.ChapterFactory;
import com.xidian.yetwish.reading.framework.utils.Constant;
import com.xidian.yetwish.reading.ui.main.adapter.ChapterAdapter;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.ReaderPageAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.widget.ReaderView;
import com.xidian.yetwish.reading.ui.main.adapter.viewpager.DepthPageTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本阅读activity
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

    private List<ReaderView> readerList = new ArrayList<>();

//    private ReaderView mReaderView;

    private ViewPager mViewPager;

    private List<String> mChapters = new ArrayList<>();

    private CommonAdapter<String> mChapterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideStatusBar();
        setContentView(R.layout.activity_reader);
        for (int i = 0; i < 10; i++) {
            readerList.add(new ReaderView(ReaderActivity.this));
        }
        initView();
        initData();
    }

    private void initData() {
        try {
            ChapterFactory.getInstance().concurrentReadFile(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/QQfile_recv/大荒蛮神.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChapterFactory.getInstance().setChapterGeneratorListener(new ChapterFactory.OnChapterGeneratorListener() {
            @Override
            public void onChapterGenerated(ImmutableList<String> chapterList) {
                mChapters.clear();
                mChapters.addAll(chapterList);
                mChapterAdapter.notifyDataSetChanged();
            }
        });


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
        mViewPager.setAdapter(new ReaderPageAdapter(ReaderActivity.this, readerList,
                new ReaderPageAdapter.OnClickListener() {
                    @Override
                    public void onClick() {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }));

//        mReaderView = (ReaderView) findViewById(R.id.mReaderView);

//        关闭滑动手势
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

//        mViewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

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


    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(lvChapter)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}
