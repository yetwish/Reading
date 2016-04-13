package com.xidian.yetwish.reading.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xidian.yetwish.reading.R;

/**
 * slide menu
 * Created by Yetwish on 2016/4/13 0013.
 */
public class SlideMenu extends RelativeLayout {

    public final static int MENU_READING = 0x01;
    public final static int MENU_NOTE = 0x02;
    public final static int MENU_READ = 0x03;

    private final static String TAG = SlideMenu.class.getSimpleName();

    private Context mContext;
    private RadioGroup rgMenu;

    private RadioButton menuReading;
    private RadioButton menuNote;
    private RadioButton menuRead;

    private TextView menuUpdate;
    private TextView menuAbout;
    private TextView menuSetting;

    private OnMenuItemClickListener mMenuListener;

    /**
     * when first init radio button check item, should not invoke checkChange
     */
    private boolean isInit = true;

    public SlideMenu(Context context) {
        super(context);
        this.mContext = context;
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.slide_menu, this);
        rgMenu = (RadioGroup) findViewById(R.id.rgMenu);
        menuReading = (RadioButton) rgMenu.findViewById(R.id.menuReading);
        menuNote = (RadioButton) rgMenu.findViewById(R.id.menuNote);
        menuRead = (RadioButton) rgMenu.findViewById(R.id.menuRead);

        menuAbout = (TextView) findViewById(R.id.menuAbout);
        menuSetting = (TextView) findViewById(R.id.menuSetting);
        menuUpdate = (TextView) findViewById(R.id.menuUpdate);

        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mMenuListener != null && !isInit) {
                    mMenuListener.onMenuItemChanged(checkedId);
                    Log.w(TAG,"check changed!");
                }
            }
        });
        menuReading.setOnClickListener(mClickListener);
        menuRead.setOnClickListener(mClickListener);
        menuNote.setOnClickListener(mClickListener);
        menuAbout.setOnClickListener(mClickListener);
        menuUpdate.setOnClickListener(mClickListener);
        menuSetting.setOnClickListener(mClickListener);

    }

    public void setMenuItemClickListener(OnMenuItemClickListener listener) {
        mMenuListener = listener;
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMenuListener != null) {
                mMenuListener.onSelectSameItem();
                Log.w(TAG,"same item!");
            }
        }
    };


    public void setSelectedItemIndex(int itemIndex){
        if(itemIndex < MENU_READING || itemIndex > MENU_READ){
            throw new IllegalArgumentException(
                    "the index of menu item should between 1 to 3.");
        }
        isInit = true;
        Log.w(TAG," itemIndex :" +itemIndex);
        switch (itemIndex){
            case MENU_READING:
                menuReading.setChecked(true);
                break;
            case MENU_NOTE:
                menuNote.setChecked(true);
                break;
            case MENU_READ:
                menuRead.setChecked(true);
                break;
        }
        isInit = false;
    }

    public interface OnMenuItemClickListener {

        /**
         * when select a different item of current item.
         * @param menuId
         */
        void onMenuItemChanged(int menuId);

        /**
         * when select the same item
         */
        void onSelectSameItem();
    }


}
