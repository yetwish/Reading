package com.xidian.yetwish.reading.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xidian.yetwish.reading.R;

/**
 * base activity with a toolbar
 * Created by Yetwish on 2016/4/14 0014.
 */
public class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;
    protected LinearLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        initActionBar();
    }

    private void initActionBar() {
        mRootView = (LinearLayout) findViewById(R.id.llRoot);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(ToolbarActivity.this, R.color.colorWhite));
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_36dp);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void setMainLayout(int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, null);
        mRootView.addView(view);
    }
}
