package com.xidian.yetwish.reading.ui.vo;

/**
 * Created by Yetwish on 2016/4/11 0011.
 */

/**
 * item of menu,consists of an icon and a text
 */
public class MenuItem {

    private int mIconResId;
    private String mText;

    public MenuItem(int iconResId, String text) {
        this.mIconResId = iconResId;
        this.mText = text;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int mIconResId) {
        this.mIconResId = mIconResId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }
}