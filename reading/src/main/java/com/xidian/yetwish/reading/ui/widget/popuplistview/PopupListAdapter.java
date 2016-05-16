package com.xidian.yetwish.reading.ui.widget.popuplistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baobomb on 2015/9/25.
 */
public class PopupListAdapter extends BaseAdapter {

    List<? extends PopupView> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = items.get(i).getItemView();
        return view;
    }

    public void setItems(List<? extends PopupView> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
