package com.xidian.yetwish.reading.ui.file_explorer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.utils.ToastUtils;
import com.xidian.yetwish.reading.utils.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.utils.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.utils.common_adapter.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo ui
 * Created by Yetwish on 2016/4/14 0014.
 */
public class FileExplorerAdapter extends CommonAdapter<File> {

    //filePath -> isCheck
    private Map<String, Boolean> mCheckMap;

    private static final String UPPER_DIR_NAME = "...";
    private static final String ROOT_DIR = "/";

    private String mCurrentPath;

    private OnFilePathChangedListener mFilePathListener;


    public void setFilePathChangedListener(OnFilePathChangedListener listener) {
        this.mFilePathListener = listener;
    }

    public FileExplorerAdapter(Context context, File rootFile) {
        super(context, R.layout.item_file_list, null);
        mData = getFileListFromRoot(rootFile);
        mCurrentPath = rootFile.getParent()+File.separator;
        mCheckMap = new HashMap<>();
        addUpFolder();
        setItemClickListener(new OnItemClickListener<File>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, File data, int position) {
                if (data != null && data.getName().equals(UPPER_DIR_NAME)) {
                    File file = new File(mCurrentPath);
                    file = file.getParentFile();
//                    LogUtils.w(file.getAbsolutePath());
                    mData.clear();
                    Collections.addAll(mData, file.listFiles());
                    if (!file.getPath().equals(ROOT_DIR))
                        mCurrentPath = file.getPath() + File.separator;
                    else mCurrentPath = file.getPath();
                    addUpFolder();
                    notifyDataSetChanged();
                } else if (data.isDirectory()) {
//                    LogUtils.w(data.getAbsolutePath() + ","  + data.getParent());
                    mData.clear();
                    if (data.listFiles() != null)
                        Collections.addAll(mData, data.listFiles());
                    mCurrentPath = data.getAbsolutePath() + File.separator;
                    addUpFolder();
                    notifyDataSetChanged();
                }
                if (mFilePathListener != null) {
                    mFilePathListener.onFilePathChanged(mCurrentPath);
                }
                ToastUtils.showShort(mContext, data.getName() + ", " + position);
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, File data, int position) {
                ToastUtils.showShort(mContext, "Long " + position);
            }
        });
    }

    private List<File> getFileListFromRoot(File rootFile) {
        List<File> files = new ArrayList<>();
        Collections.addAll(files, rootFile);
        return files;
    }


    private void addUpFolder() {
        if (mCurrentPath.equals(ROOT_DIR))
            return;
        File file = new File(mCurrentPath, UPPER_DIR_NAME);
        mData.add(0, file);
    }

    @Override
    public void convert(final ViewHolder holder, final File file) {
        holder.setText(R.id.tvItemFileTitle, file.getName());

        if (file.isDirectory() || file.getName().equals(UPPER_DIR_NAME)) {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_folder_gray_48dp)
                    .setText(R.id.tvItemFileSize, "Folder")
                    .setVisible(R.id.cbItemFileChose, false);
        } else {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_insert_drive_file_gray_48dp)
                    .setText(R.id.tvItemFileSize, file.getAbsolutePath() + "")
                    .setVisible(R.id.cbItemFileChose, true);

            //update the state of checkBox
            if (mCheckMap.containsKey(file.getAbsolutePath()))
                holder.setChecked(R.id.cbItemFileChose, mCheckMap.get(file.getAbsolutePath()));
            else holder.setChecked(R.id.cbItemFileChose, false);


        }

        ((CheckBox) holder.getView(R.id.cbItemFileChose)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //use holder.getLayoutPosition() to get the latest position of data
                mCheckMap.put(mData.get(holder.getLayoutPosition()).getAbsolutePath(), isChecked);
            }
        });
    }


    public interface OnFilePathChangedListener {

        void onFilePathChanged(String path);
    }
}
