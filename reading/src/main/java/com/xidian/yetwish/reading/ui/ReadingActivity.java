package com.xidian.yetwish.reading.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xidian.yetwish.reading.R;

/**
 * main activity , use toolbar and drawerLayout to implement slideMenu
 */
public class ReadingActivity extends SlideMenuActivity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReadingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_reading);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_READING);
    }
}
