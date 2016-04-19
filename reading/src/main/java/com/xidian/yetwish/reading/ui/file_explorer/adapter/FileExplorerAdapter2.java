package com.xidian.yetwish.reading.ui.file_explorer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.utils.LogUtils;
import com.xidian.yetwish.reading.utils.ToastUtils;
import com.xidian.yetwish.reading.utils.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.utils.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.utils.common_adapter.ViewHolder;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo data
 * Created by Yetwish on 2016/4/14 0014.
 */
public class FileExplorerAdapter2 extends CommonAdapter<File> {

    //filePath -> isCheck
    private Map<String, Boolean> mCheckMap;

    public FileExplorerAdapter2(Context context, List<File> data) {
        super(context, R.layout.item_file_list, data);
        mCheckMap = new HashMap<>();
        addUpFolder();
        setItemClickListener(new OnItemClickListener<File>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, File data, int position) {
                if (data != null && data.getName().equals("...")) {
                    File file = data.getParentFile().getParentFile();
                    LogUtils.w(file.getAbsolutePath() + "," + file.getName());
                    mData.clear();
                    Collections.addAll(mData, file.listFiles());
                    addUpFolder();
                } else if (data.isDirectory()) {
                    mData.clear();
                    if (data.listFiles() != null)
                        Collections.addAll(mData, data.listFiles());
                    addUpFolder();
                    notifyDataSetChanged();
                }
                ToastUtils.showShort(mContext, data.getName() + ", " + position);
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, File data, int position) {
                ToastUtils.showShort(mContext, "Long " + position);
            }
        });
    }

    private void addUpFolder() {
        File file = new File(mData.get(0).getParentFile().getPath(), "...");
        mData.add(0, file);
    }

    @Override
    public void convert(final ViewHolder holder, final File fileInfo) {
        holder.setText(R.id.tvItemFileTitle, fileInfo.getName());

        if (fileInfo.isDirectory()) {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_folder_gray_48dp)
                    .setText(R.id.tvItemFileSize, "Folder")
                    .setVisible(R.id.cbItemFileChose, false);
        } else {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_insert_drive_file_gray_48dp)
                    .setText(R.id.tvItemFileSize, fileInfo.getAbsolutePath() + "")
                    .setVisible(R.id.cbItemFileChose, true);

            //update the state of checkBox
            if (mCheckMap.containsKey(fileInfo.getAbsolutePath()))
                holder.setChecked(R.id.cbItemFileChose, mCheckMap.get(fileInfo.getAbsolutePath()));
            holder.setChecked(R.id.cbItemFileChose, false);


        }

        ((CheckBox) holder.getView(R.id.cbItemFileChose)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //use holder.getLayoutPosition() to get the latest position of data
                mCheckMap.put(mData.get(holder.getLayoutPosition()).getAbsolutePath(), isChecked);
            }
        });
    }

}
