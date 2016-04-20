package com.xidian.yetwish.reading.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.xidian.yetwish.reading.utils.LogUtils;

/**
 * Created by Yetwish on 2016/4/20 0020.
 */
public class EmptyRecyclerView extends RecyclerView {

    private View mEmptyView;

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        LogUtils.w("empty: "+this.mEmptyView.toString());
    }

    private void checkIfEmpty() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
        }
    }

    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }
    };

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        final Adapter oldAdapter = getAdapter();

        if (oldAdapter != null)
            oldAdapter.unregisterAdapterDataObserver(observer);

        if (adapter != null)
            adapter.registerAdapterDataObserver(observer);

        super.swapAdapter(adapter, removeAndRecycleExistingViews);

        checkIfEmpty();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();

        if (oldAdapter != null)
            oldAdapter.unregisterAdapterDataObserver(observer);

        super.setAdapter(adapter);

        if (adapter != null)
            adapter.registerAdapterDataObserver(observer);

    }
}
