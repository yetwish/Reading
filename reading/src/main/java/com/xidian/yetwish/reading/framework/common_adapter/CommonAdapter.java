package com.xidian.yetwish.reading.framework.common_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * common adapter
 * Created by Yetwish on 2016/4/14 0014.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected int mLayoutId;
    protected Context mContext;
    protected List<T> mData;
    protected LayoutInflater mInflater;

    private OnItemClickListener<T> mItemClickListener;


    public CommonAdapter(Context context, int layoutId, List<T> data) {
        mContext = context;
        mLayoutId = layoutId;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        setListener(parent, holder, viewType);
        return holder;
    }

    protected boolean isEnable(int viewType) {
        return true;
    }

    public void setItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    protected void setListener(final ViewGroup parent, final ViewHolder holder, final int viewType) {
        if (!isEnable(viewType)) return;
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int position = getPosition(holder);
                    mItemClickListener.onItemClick(parent, v, mData.get(position), position);
                }
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null) {
                    int position = getPosition(holder);
                    mItemClickListener.onItemLongClick(parent, v, mData.get(position), position);
                }
                return true;
            }
        });
    }

    protected int getPosition(ViewHolder holder) {
        return holder.getAdapterPosition();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        convert(holder, mData.get(position));
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
