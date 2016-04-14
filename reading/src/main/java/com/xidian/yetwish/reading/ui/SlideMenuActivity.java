package com.xidian.yetwish.reading.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.xidian.yetwish.reading.R;

/**
 * activity with a slide menu and a toolbar
 * Created by Yetwish on 2016/4/12 0012.
 */
public class SlideMenuActivity extends BaseActivity {


    protected Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mMainLayout;

    protected SlideMenu mSlideMenu;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_slide_menu);
        initSlideMenu();
    }

    private void initSlideMenu() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mSlideMenu = (SlideMenu) findViewById(R.id.slide_menu);
        mSlideMenu.setMenuItemClickListener(new SlideMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemChanged(int menuId) {
                mDrawerLayout.closeDrawers();
                switch (menuId) {
                    case R.id.menuReading:
                        ReadingActivity.startActivity(mContext);
                        break;
                    case R.id.menuNote:
                        NoteActivity.startActivity(mContext);
                        break;
                    case R.id.menuRead:
                        ReadActivity.startActivity(mContext);
                        break;
                    case R.id.menuAbout:
                        //TODO
                        break;
                    case R.id.menuSetting:
                        break;
                    case R.id.menuUpdate:
                        break;

                }
            }

            @Override
            public void onSelectSameItem() {
                mDrawerLayout.closeDrawers();
            }

        });

        mMainLayout = (RelativeLayout) findViewById(R.id.rlMainFrame);
    }


    protected void setMainLayout(int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, null, false);
        if (view != null)
            mMainLayout.addView(view);
    }


}
