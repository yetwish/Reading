package com.xidian.yetwish.reading.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;

import java.util.List;

/**
 *
 * Created by Yetwish on 2016/4/24 0024.
 */
public class ChapterAdapter extends CommonAdapter<ChapterVo> {


    public ChapterAdapter(Context context, List<ChapterVo> data, final IReaderProgressChangeListener listener) {
        super(context, R.layout.item_chapter_list, data);
        setItemClickListener(new OnItemClickListener<ChapterVo>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, ChapterVo data, int position) {
                if (listener != null)
                    listener.onProgressChanged(position,data.getFirstCharPosition());
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, ChapterVo data, int position) {
                //do nothing
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, ChapterVo data) {
        holder.setText(R.id.tvItemChapterName, data.getName())
                .setText(R.id.tvItemPosition, data.getFirstCharPosition() + "");
    }

}
