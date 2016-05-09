package com.xidian.yetwish.reading.ui.main.adapter.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;
import com.xidian.yetwish.reading.ui.widget.ReaderView;

import java.util.ArrayList;
import java.util.List;

/**
 * view pager adapter
 * Created by Yetwish on 2016/4/25 0025.
 */
public class ReaderPageAdapter extends PagerAdapter implements View.OnClickListener {

    private Context mContext;
    private List<ReaderView> mViews;
    private List<PageVo> mData;

    private static final int DEFAULT_INDEX = -1;

    public interface OnReaderViewClickListener {
        void onClick();
    }

    private OnReaderViewClickListener mClickListener;

    public void setOnClickListener(OnReaderViewClickListener listener) {
        mClickListener = listener;
    }


    public ReaderPageAdapter(Context context, List<PageVo> data) {
        this.mContext = context;
        this.mData = data;
        refreshAllViews();
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        view.setOnClickListener(this);
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent != null)
            parent.removeAllViews();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }


    public void refreshAllViews() {
        if (mViews == null)
            mViews = new ArrayList<>(mData.size());
        mViews.clear();
        notifyDataSetChanged();
        for (PageVo page : mData) {
            ReaderView view = new ReaderView(mContext);
            view.setData(page);
            mViews.add(view);
        }
        notifyDataSetChanged();
    }

    public void addPages(ImmutableList<PageVo> pageList) {
        addPages(DEFAULT_INDEX, pageList);
    }


    public void addPages(int location, ImmutableList<PageVo> pageList) {
        if (mViews == null)
            return;
        List<ReaderView> views = new ArrayList<>(pageList.size());
        for (PageVo page : pageList) {
            ReaderView view = new ReaderView(mContext);
            view.setData(page);
            views.add(view);
        }
        if (location == DEFAULT_INDEX) {
            mData.addAll(pageList);
            mViews.addAll(views);
        } else {
            mData.addAll(location, pageList);
            mViews.addAll(location, views);
        }
        notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null)
            mClickListener.onClick();
    }


}
