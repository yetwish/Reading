package com.xidian.yetwish.reading.ui.main.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;

import java.util.List;

/**
 * Created by Yetwish on 2016/4/24 0024.
 */
public class ChapterAdapter extends CommonAdapter<ChapterVo> {


    public ChapterAdapter(Context context, List<ChapterVo> data) {
        super(context, android.R.layout.simple_list_item_1, data);
    }

    @Override
    public void convert(ViewHolder holder, ChapterVo data) {
        holder.setText(android.R.id.text1, data.getName())
                .setBackgroundColor(android.R.id.text1, ContextCompat.getColor(mContext, R.color.colorWhite));
    }
}
