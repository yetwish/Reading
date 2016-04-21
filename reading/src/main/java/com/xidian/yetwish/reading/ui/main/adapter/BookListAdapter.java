package com.xidian.yetwish.reading.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.database.bean.Book;
import com.xidian.yetwish.reading.utils.LogUtils;
import com.xidian.yetwish.reading.utils.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.utils.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.utils.common_adapter.ViewHolder;

import java.util.List;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class BookListAdapter extends CommonAdapter<Book> {


    public BookListAdapter(Context context, List<Book> data) {
        super(context, R.layout.item_book, data);
        setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object data, int position) {

            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, Object data, int position) {
                ImageView ivDelete = (ImageView) view.findViewById(R.id.ivBookDelete);
                ivDelete.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, Book book) {
        holder.setImageResource(R.id.ivBookIcon,book.getIconResId())
                .setText(R.id.tvBookName,book.getName())
                .setText(R.id.tvBookAuthor,book.getAuthor())
                .setNumberProgress(R.id.pbBookProgress,book.getProgress())
                .setOnClickListener(R.id.ivBookDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.w("delete click!");
                        v.setVisibility(View.GONE);
                    }
                });
    }



}
