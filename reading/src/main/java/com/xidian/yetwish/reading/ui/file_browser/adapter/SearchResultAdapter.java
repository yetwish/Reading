package com.xidian.yetwish.reading.ui.file_browser.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.file_browser.SearchResultItemSupport;
import com.xidian.yetwish.reading.framework.utils.ToastUtils;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.SectionAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * search result adapter
 * Created by Yetwish on 2016/4/14 0014.
 */
public class SearchResultAdapter extends SectionAdapter<File> {

    private Map<String, Boolean> mCheckMap;

    public SearchResultAdapter(Context context, List<File> data) {
        super(context, R.layout.item_file_list, data, new SearchResultItemSupport());
        mCheckMap = new HashMap<>();
        setItemClickListener(new OnItemClickListener<File>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, File data, int position) {
                if (data == null) return;
                //todo open file
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, File data, int position) {
                ToastUtils.showShort(mContext, "Long " + position);
            }
        });
    }

    @Override
    public void convert(final ViewHolder holder, File file) {
        holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_insert_drive_file_gray_48dp)
                .setText(R.id.tvItemFileTitle, file.getName())
                .setText(R.id.tvItemFileSize, file.getTotalSpace() + "")
                .setVisible(R.id.cbItemFileChose, true);
        if (mCheckMap.containsKey(file.getAbsolutePath()))
            holder.setChecked(R.id.cbItemFileChose, mCheckMap.get(file.getAbsolutePath()));
        else holder.setChecked(R.id.cbItemFileChose, false);

        ((CheckBox) holder.getView(R.id.cbItemFileChose)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //use holder.getLayoutPosition() to get the latest position of data
                mCheckMap.put(mData.get(getIndexForPosition(holder.getLayoutPosition())).getAbsolutePath(), isChecked);
            }
        });
    }

    public List<File> getCheckFiles() {
        List<File> files = new ArrayList<File>();
        for(String path: mCheckMap.keySet()){
            files.add(new File(path));
        }
        return files;
    }
}
