package com.xidian.yetwish.reading.ui.main.adapter.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.xidian.yetwish.reading.ui.widget.ReaderView;

/**
 * view pager adapter
 * Created by Yetwish on 2016/4/25 0025.
 */
public class ReaderPageAdapter extends PagerAdapter {

    private Context mContext;
    public ReaderPageAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = new ReaderView(mContext);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
