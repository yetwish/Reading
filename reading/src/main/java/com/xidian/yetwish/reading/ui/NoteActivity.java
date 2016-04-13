package com.xidian.yetwish.reading.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xidian.yetwish.reading.R;

/**
 * note activity , shows various notes of books
 */
public class NoteActivity extends SlideMenuActivity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NoteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_note);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_NOTE);
    }
}
