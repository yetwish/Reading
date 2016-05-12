package com.xidian.yetwish.reading.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemLongClickListener;
import com.xidian.yetwish.reading.framework.utils.BitmapUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;
import com.xidian.yetwish.reading.ui.reader.ReaderActivity;

import java.util.List;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class BookListAdapter extends CommonAdapter<BookVo> {

    private int iconHeight;
    private int iconWidth;

    private View deleteView;

    private OnItemLongClickListener<BookVo> mLongClickListener;

    public void setItemLongClickListener(OnItemLongClickListener<BookVo> listener) {
        this.mLongClickListener = listener;
    }

    public BookListAdapter(final Context context, List<BookVo> data) {
        super(context, R.layout.item_book_list, data);
        setItemClickListener(new OnItemClickListener<BookVo>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, BookVo data, int position) {
                if (deleteView != null && deleteView.isShown()) {
                    deleteView.setVisibility(View.GONE);
                }
                ReaderActivity.startActivity(context, data);
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, BookVo data, int position) {
                ImageView ivDelete = (ImageView) view.findViewById(R.id.ivBookDelete);
                ivDelete.setVisibility(View.VISIBLE);
                deleteView = ivDelete;
                if (mLongClickListener != null)
                    mLongClickListener.onItemLongClick(ivDelete, data, position);
            }
        });
        iconWidth = mContext.getResources().getDimensionPixelSize(R.dimen.item_icon_width);
        iconHeight = mContext.getResources().getDimensionPixelSize(R.dimen.item_icon_height);
    }

    @Override
    public void convert(ViewHolder holder, BookVo book) {
        holder.setText(R.id.tvBookName, book.getName())
                .setText(R.id.tvBookAuthor, book.getAuthor())
                .setNumberProgress(R.id.pbBookProgress, book.getProgress())
                .setVisible(R.id.ivBookDelete,false);
        if (book.getIconResId() != 0) {
            holder.setImageResource(R.id.ivBookIcon, book.getIconResId());
        } else {
            int resId = BitmapUtils.getRandomAppBarBgImageRes();
            book.setIconResId(resId);
            holder.setImageBitmap(R.id.ivBookIcon, BitmapUtils.decodeSampleBitmapFromResource(
                    mContext.getResources(), resId, iconWidth, iconHeight));
        }
    }


}
