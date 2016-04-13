package com.xidian.yetwish.reading.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xidian.yetwish.reading.R;

/**
 * read activity ,shows all the book that has been read
 */
public class ReadActivity extends SlideMenuActivity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_read);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_READ);
    }
}
