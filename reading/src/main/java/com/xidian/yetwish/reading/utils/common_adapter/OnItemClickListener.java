package com.xidian.yetwish.reading.utils.common_adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * adapter item click listener: listen click and longClick event
 * Created by Yetwish on 2016/4/14 0014.
 */
public interface OnItemClickListener<T> {

    void onItemClick(ViewGroup parent, View view, T data, int position);

    void onItemLongClick(ViewGroup parent, View view, T data, int position);
}