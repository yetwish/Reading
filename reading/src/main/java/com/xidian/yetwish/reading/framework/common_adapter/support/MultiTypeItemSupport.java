package com.xidian.yetwish.reading.framework.common_adapter.support;

/**
 *
 * Created by Yetwish on 2016/4/14 0014.
 */
public interface MultiTypeItemSupport<T> {

    int getLayoutId(int itemType);

    int getItemViewType(int position,T t);
}
