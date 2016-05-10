package com.xidian.yetwish.reading.ui.com.baobomb.popuplistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Baobomb on 2015/9/25.
 */
public abstract class PopupView<T> {

    private LayoutInflater mInflater;
    private T mData;
    private View mItemView;
    private View mPopupView;
    private View mExtendView;

    public PopupView(Context context, int resId, T data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        mItemView = mInflater.inflate(resId, null);
        mPopupView = mInflater.inflate(resId,null); //保留两个副本，一个用于listView item,一个作为popupView
        setItemView(mItemView, mData);
        setItemView(mPopupView, mData);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mExtendView = setExtendView(mExtendView, mData);
        mExtendView.setLayoutParams(layoutParams);
    }


    public View getItemView(){
        return mItemView;
    }

    public View getPopupView() {
        return mPopupView;
    }

    public View getExtendView() {
        return mExtendView;
    }

    public abstract void setItemView(View view, T data);

    public abstract View setExtendView(View view, T data);
}
