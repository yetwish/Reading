package com.xidian.yetwish.reading.ui.file_explorer.adapter;

import android.content.Context;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.file_explorer.SearchResultItemSupport;
import com.xidian.yetwish.reading.utils.common_adapter.SectionAdapter;
import com.xidian.yetwish.reading.utils.common_adapter.ViewHolder;

import java.io.File;
import java.util.List;

/**
 * search result adapter
 * Created by Yetwish on 2016/4/14 0014.
 */
public class SearchResultAdapter extends SectionAdapter<File> {

    public SearchResultAdapter(Context context, int layoutId, List<File> data) {
        super(context, layoutId, data, new SearchResultItemSupport());
    }

    @Override
    public void convert(ViewHolder holder, File file) {
        if (!file.isDirectory()) {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_insert_drive_file_gray_48dp)
                    .setText(R.id.tvItemFileTitle, file.getName())
                    .setText(R.id.tvItemFileSize, file.getTotalSpace() + "");
        }

    }
}
