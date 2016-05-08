package com.xidian.yetwish.reading.framework.common_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xidian.yetwish.reading.framework.common_adapter.support.MultiTypeItemSupport;
import com.xidian.yetwish.reading.framework.common_adapter.support.SectionSupport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Yetwish on 2016/4/19 0019.
 */
public abstract class SectionAdapter<T> extends MultiTypeItemCommonAdapter<T> {

    private SectionSupport mSectionSupport;
    private static final int TYPE_SECTION = 0;
    private LinkedHashMap<String, Integer> mSections;

    private MultiTypeItemSupport<T> headerItemTypeSupport;

    @Override
    public int getItemViewType(int position) {
        return mMultiTypeItemSupport.getItemViewType(position, null);
    }

    final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            findSections();
        }
    };

    public SectionAdapter(Context context, int layoutId, List<T> data, SectionSupport sectionSupport) {
        this(context, layoutId, null, data, sectionSupport);
    }

    public SectionAdapter(Context context, MultiTypeItemSupport multiItemTypeSupport, List<T> data, SectionSupport sectionSupport) {
        this(context, -1, multiItemTypeSupport, data, sectionSupport);
    }

    public SectionAdapter(Context context, int layoutId, MultiTypeItemSupport multiItemTypeSupport, List<T> data, SectionSupport sectionSupport) {
        super(context, data, null);
        mLayoutId = layoutId;
        mSectionSupport = sectionSupport;
        mSections = new LinkedHashMap<>();
        findSections();
        initMultiItemTypeSupport(layoutId, mMultiTypeItemSupport);
        mMultiTypeItemSupport = headerItemTypeSupport;
        registerAdapterDataObserver(observer);
    }

    private MultiTypeItemSupport<T> initMultiItemTypeSupport(int layoutId, final MultiTypeItemSupport multiItemTypeSupport) {
        if (layoutId != -1) {
            headerItemTypeSupport = new MultiTypeItemSupport<T>() {
                @Override
                public int getLayoutId(int itemType) {
                    if (itemType == TYPE_SECTION)
                        return mSectionSupport.sectionHeaderLayoutId();
                    else
                        return mLayoutId;
                }

                @Override
                public int getItemViewType(int position, T o) {
                    int positionVal = getIndexForPosition(position);
                    return mSections.values().contains(position) ?
                            TYPE_SECTION :
                            1;
                }
            };
        } else if (multiItemTypeSupport != null) {
            headerItemTypeSupport = new MultiTypeItemSupport<T>() {
                @Override
                public int getLayoutId(int itemType) {
                    if (itemType == TYPE_SECTION)
                        return mSectionSupport.sectionHeaderLayoutId();
                    else
                        return multiItemTypeSupport.getLayoutId(itemType);
                }

                @Override
                public int getItemViewType(int position, T o) {
                    int positionVal = getIndexForPosition(position);
                    return mSections.values().contains(position) ?
                            TYPE_SECTION :
                            multiItemTypeSupport.getItemViewType(positionVal, o);
                }
            };
        } else {
            throw new RuntimeException("layoutId or MultiItemTypeSupport must set one.");
        }
        return headerItemTypeSupport;
    }

    @Override
    protected boolean isEnable(int viewType) {
        if (viewType == TYPE_SECTION)
            return false;
        return super.isEnable(viewType);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }

    public void findSections() {
        int n = mData.size();
        int nSections = 0;
        mSections.clear();

        for (int i = 0; i < n; i++) {
            String sectionName = mSectionSupport.getTitle(mData.get(i));

            if (!mSections.containsKey(sectionName)) {
                mSections.put(sectionName, i + nSections);
                nSections++;
            }
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + mSections.size();
    }

    public int getIndexForPosition(int position) {
        int nSections = 0;

        Set<Map.Entry<String, Integer>> entrySet = mSections.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() < position) {
                nSections++;
            }
        }
        return position - nSections;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        position = getIndexForPosition(position);
        if (holder.getItemViewType() == TYPE_SECTION) {
            holder.setText(mSectionSupport.sectionTitleTextViewId(), mSectionSupport.getTitle(mData.get(position)));
            return;
        }
        super.onBindViewHolder(holder, position);
    }


}
