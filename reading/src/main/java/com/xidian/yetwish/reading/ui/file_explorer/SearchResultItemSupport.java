package com.xidian.yetwish.reading.ui.file_explorer;

import com.xidian.yetwish.reading.utils.common_adapter.support.SectionSupport;

import java.io.File;

/**
 * Created by Yetwish on 2016/4/15 0015.
 */
public class SearchResultItemSupport implements SectionSupport<File> {

    public SearchResultItemSupport() {

    }

    @Override
    public int sectionHeaderLayoutId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public int sectionTitleTextViewId() {
        return android.R.id.text1;
    }

    @Override
    public String getTitle(File file) {
        return file.getPath();
    }
}
