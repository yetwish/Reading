package com.xidian.yetwish.reading.ui.add_book.auto_search;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.common_adapter.SectionAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * search result adapter  todo setListener position
 * Created by Yetwish on 2016/4/14 0014.
 */
public class SearchResultAdapter extends SectionAdapter<File> {

    private Map<String, Boolean> mCheckMap;
    private List<String> mAddedFiles;

    public SearchResultAdapter(Context context, List<File> data) {
        super(context, R.layout.item_file_list, data, new SearchResultItemSupport());
        mCheckMap = new HashMap<>();
        //get added bookList
        ImmutableList<BookVo> addedBooks = DatabaseManager.getsInstance().getBookManager().queryAll();
        mAddedFiles = new ArrayList<>(addedBooks.size());
        for (BookVo book : addedBooks) {
            mAddedFiles.add(book.getFilePath());
        }

    }

    @Override
    public void convert(final ViewHolder holder, File file) {
        holder.setVisible(R.id.cbItemFileChose, false)
                .setVisible(R.id.tvItemHasAdded, false);

        holder.setImageResource(R.id.ivItemFileIcon, R.mipmap.ic_insert_drive_file_gray_48dp)
                .setText(R.id.tvItemFileTitle, file.getName())
                .setText(R.id.tvItemFileSize, file.getTotalSpace() + "")
                .setChecked(R.id.cbItemFileChose, false);

        CheckBox cb = holder.getView(R.id.cbItemFileChose);
        if (mAddedFiles!= null &&!mAddedFiles.contains(file.getAbsolutePath())) {
            //未导入
            cb.setVisibility(View.VISIBLE);
        } else {
            holder.setVisible(R.id.tvItemHasAdded, true);
        }

        if (cb.getVisibility() != View.VISIBLE)
            return;

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

    public ImmutableList<File> getCheckFiles() {
        List<File> files = new ArrayList<File>();
        for (String path : mCheckMap.keySet()) {
            files.add(new File(path));
        }
        return ImmutableList.copyOf(files);
    }
}
