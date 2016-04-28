package com.xidian.yetwish.reading.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;
import com.xidian.yetwish.reading.ui.main.ReaderActivity;

import java.util.List;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class BookListAdapter extends CommonAdapter<BookVo> {


    public BookListAdapter(final Context context, List<BookVo> data) {
        super(context, R.layout.item_book, data);
        setItemClickListener(new OnItemClickListener<BookVo>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, BookVo data, int position) {
                ReaderActivity.startActivity(context,data.getFilePath());
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, BookVo data, int position) {
                ImageView ivDelete = (ImageView) view.findViewById(R.id.ivBookDelete);
                ivDelete.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, BookVo bookVo) {
        holder.setImageResource(R.id.ivBookIcon, bookVo.getIconResId())
                .setText(R.id.tvBookName, bookVo.getName())
                .setText(R.id.tvBookAuthor, bookVo.getAuthor())
                .setNumberProgress(R.id.pbBookProgress, bookVo.getProgress())
                .setOnClickListener(R.id.ivBookDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.w("delete click!");
                        v.setVisibility(View.GONE);
                    }
                });
    }



}
