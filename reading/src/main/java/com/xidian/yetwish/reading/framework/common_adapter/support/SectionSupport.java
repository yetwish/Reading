package com.xidian.yetwish.reading.framework.common_adapter.support;

/**
 * section support for section adapter
 * Created by Yetwish on 2016/4/19 0019.
 */
public interface SectionSupport<T> {
    int sectionHeaderLayoutId();

    int sectionTitleTextViewId();

    String getTitle(T t);

}
