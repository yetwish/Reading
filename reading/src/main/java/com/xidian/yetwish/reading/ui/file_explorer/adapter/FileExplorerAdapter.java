package com.xidian.yetwish.reading.ui.file_explorer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.file_explorer.FileInfo;
import com.xidian.yetwish.reading.utils.ToastUtils;
import com.xidian.yetwish.reading.utils.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.utils.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.utils.common_adapter.ViewHolder;

import java.util.List;

/**
 * todo data
 * Created by Yetwish on 2016/4/14 0014.
 */
public class FileExplorerAdapter extends CommonAdapter<FileInfo> {


    public FileExplorerAdapter(Context context, List<FileInfo> data) {
        super(context, R.layout.item_file_list, data);

        setItemClickListener(new OnItemClickListener<FileInfo>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, FileInfo data, int position) {
                ToastUtils.showShort(mContext, data.getFileName() + ", " + position);
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, FileInfo data, int position) {
                ToastUtils.showShort(mContext, "Long " + position);
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, FileInfo fileInfo) {
        holder.setText(R.id.tvItemFileTitle, fileInfo.getFileName());

        if (fileInfo.isDir()) {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_folder_gray_48dp)
                    .setText(R.id.tvItemFileSize, "Folder")
                    .setVisible(R.id.cbItemFileChose,false);
        } else {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_insert_drive_file_gray_48dp)
                    .setText(R.id.tvItemFileSize, fileInfo.getSize() + "")
                    .setVisible(R.id.cbItemFileChose, true);
        }
    }
}
