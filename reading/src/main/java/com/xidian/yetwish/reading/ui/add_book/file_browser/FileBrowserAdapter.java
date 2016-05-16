package com.xidian.yetwish.reading.ui.add_book.file_browser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.utils.FileUtils;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;
import com.xidian.yetwish.reading.ui.add_book.TXTFileFilter;

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
public class FileBrowserAdapter extends CommonAdapter<File> {

    //filePath -> isCheck
    private Map<String, Boolean> mCheckMap;

    private static final String UPPER_DIR_NAME = "...";
    private static final String ROOT_DIR = "/";

    private String mCurrentPath = ROOT_DIR;

    private TXTFileFilter mFileFilter;

    private OnFilePathChangedListener mFilePathListener;

    private List<String> mAddedFiles;

    public void setFilePathChangedListener(OnFilePathChangedListener listener) {
        this.mFilePathListener = listener;
        mFilePathListener.onFilePathChanged(mCurrentPath);
    }

    public FileBrowserAdapter(Context context, File rootFile) {
        super(context, R.layout.item_file_list, null);
        mData = new ArrayList<>();
        mCheckMap = new HashMap<>();
        mFileFilter = new TXTFileFilter();
        ImmutableList<BookVo> addedBooks = DatabaseManager.getsInstance().getBookManager().queryAll();
        mAddedFiles = new ArrayList<>(addedBooks.size());
        for (BookVo book : addedBooks) {
            mAddedFiles.add(book.getFilePath());
        }
        updateFileList(rootFile);
        setItemClickListener(new OnItemClickListener<File>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, File data, int position) {
                if (data == null) return;
                if (data.getName().equals(UPPER_DIR_NAME)) {
                    //get parent File
                    File file = new File(mCurrentPath);
                    file = file.getParentFile();
                    updateFileList(file);
                } else if (data.isDirectory()) {
                    updateFileList(data);
                }
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, File data, int position) {
            }
        });
    }


    private void updateFileList(File rootFile) {
        mData.clear();
        //get file list
        if (rootFile.listFiles(mFileFilter) != null) {
            Collections.addAll(mData, rootFile.listFiles(mFileFilter));
            mData = FileUtils.ORDERING_FILE.sortedCopy(mData);
        }
        //update mCurrentPath
        mCurrentPath = rootFile.getPath().equals(ROOT_DIR) ?
                rootFile.getPath() : rootFile.getPath() + File.separator;
        //add upper item
        addUpperFolder();
        notifyDataSetChanged();
        if (mFilePathListener != null) {
            mFilePathListener.onFilePathChanged(mCurrentPath);
        }
    }

    private void addUpperFolder() {
        if (mCurrentPath.equals(ROOT_DIR))
            return;
        File file = new File(mCurrentPath, UPPER_DIR_NAME);
        mData.add(0, file);
    }

    @Override
    public void convert(final ViewHolder holder, final File file) {
        holder.setText(R.id.tvItemFileTitle, file.getName());

        holder.setVisible(R.id.tvItemHasAdded, false)
                .setVisible(R.id.cbItemFileChose, false);

        if (file.isDirectory() || file.getName().equals(UPPER_DIR_NAME)) {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_folder_gray_48dp)
                    .setText(R.id.tvItemFileSize, "Folder")
                    .setVisible(R.id.cbItemFileChose, false);
        } else {
            holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_insert_drive_file_gray_48dp)
                    .setText(R.id.tvItemFileSize, FileUtils.getFileSize(file));

            CheckBox cb = holder.getView(R.id.cbItemFileChose);

            if (!mAddedFiles.contains(file.getAbsolutePath())) {
                //未导入
                cb.setVisibility(View.VISIBLE);
            } else {
                holder.setVisible(R.id.tvItemHasAdded, true);
            }

            if (cb.getVisibility() != View.VISIBLE)
                return;

            //update the state of checkBox
            if (mCheckMap.containsKey(file.getAbsolutePath()))
                holder.setChecked(R.id.cbItemFileChose, mCheckMap.get(file.getAbsolutePath()));
            else holder.setChecked(R.id.cbItemFileChose, false);


            ((CheckBox) holder.getView(R.id.cbItemFileChose)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //use holder.getLayoutPosition() to get the latest position of data
                    mCheckMap.put(mData.get(holder.getLayoutPosition()).getAbsolutePath(), isChecked);
                }
            });

        }
        if(file.getName().equals(UPPER_DIR_NAME)){
            holder.setText(R.id.tvItemFileSize,"上层文件夹");
        }
    }

    public ImmutableList<File> getCheckFiles() {
        List<File> files = new ArrayList<File>();
        for (String path : mCheckMap.keySet()) {
            if (mCheckMap.get(path))
                files.add(new File(path));
        }
        return ImmutableList.copyOf(files);
    }


    public interface OnFilePathChangedListener {

        void onFilePathChanged(String path);
    }
}
