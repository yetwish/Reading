package com.xidian.yetwish.reading.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.fab.OnFABItemClickListener;
import com.xidian.yetwish.reading.ui.fab.PopupMenu;

/**
 * main activity , use toolbar and drawerLayout to implement slideMenu
 */
public class ReadingActivity extends SlideMenuActivity {

    private PopupMenu mPopupMenu;
    private ImageButton FabReading;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReadingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_reading);
        FabReading = (ImageButton) findViewById(R.id.fab_reading);
        FabReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPopupMenu == null){
                    mPopupMenu = new PopupMenu(ReadingActivity.this,mFABItemClickListener);
                }
                if(!mPopupMenu.isShowing()){
                    mPopupMenu.show(mToolbar);
                }
                else {
                    mPopupMenu.hide();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_READING);
    }

    private OnFABItemClickListener mFABItemClickListener = new OnFABItemClickListener() {
        @Override
        public void onAutoSearching() {
            //TODO auto search
        }

        @Override
        public void onChoseManually() {
            //TODO chose manually
        }
    };

}
