package com.xidian.yetwish.reading.framework.common_adapter;

import android.view.View;

/**
 * Created by Yetwish on 2016/5/11 0011.
 */
public interface OnItemLongClickListener<T>  {

    void onItemLongClick(View deleteView, T data, int position);
}
