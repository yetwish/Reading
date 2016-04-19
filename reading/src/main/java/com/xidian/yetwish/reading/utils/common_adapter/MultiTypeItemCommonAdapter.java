package com.xidian.yetwish.reading.utils.common_adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.xidian.yetwish.reading.utils.common_adapter.support.MultiTypeItemSupport;

import java.util.List;

/**
 *
 * Created by Yetwish on 2016/4/14 0014.
 */
public abstract class MultiTypeItemCommonAdapter<T> extends CommonAdapter<T> {

    protected MultiTypeItemSupport<T> mMultiTypeItemSupport;

    public MultiTypeItemCommonAdapter(Context context, List<T> data, MultiTypeItemSupport<T> support) {
        super(context, -1, data);
        mMultiTypeItemSupport = support;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeItemSupport != null)
            return mMultiTypeItemSupport.getItemViewType(position, mData.get(position));
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mMultiTypeItemSupport == null)
            return super.onCreateViewHolder(parent, viewType);

        int layoutId = mMultiTypeItemSupport.getLayoutId(viewType);
        ViewHolder holder = ViewHolder.get(mContext, null, parent, layoutId, -1);
        setListener(parent,holder,viewType);
        return holder;
    }
}
