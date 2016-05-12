package com.xidian.yetwish.reading.ui.reader.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;
import com.xidian.yetwish.reading.framework.vo.reader.BookmarkVo;
import com.xidian.yetwish.reading.ui.reader.IReaderProgressChangeListener;

import java.util.List;

/**
 * 书签list adapter
 * Created by Yetwish on 2016/5/12 0012.
 */
public class BookmarkAdapter extends CommonAdapter<BookmarkVo> {

    public BookmarkAdapter(Context context, List<BookmarkVo> data, final IReaderProgressChangeListener listener) {
        super(context, R.layout.item_chapter_list, data);
        setItemClickListener(new OnItemClickListener<BookmarkVo>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, BookmarkVo data, int position) {
                if (listener != null)
                    listener.onProgressChanged(data.getChapterIndex(), data.getLastCharPosition(),true);
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, BookmarkVo data, int position) {

            }
        });
    }

    @Override
    public void convert(ViewHolder holder, BookmarkVo data) {
        holder.setText(R.id.tvItemChapterName, data.getName())
                .setText(R.id.tvItemPosition, data.getLastCharPosition() + "");
    }
}
