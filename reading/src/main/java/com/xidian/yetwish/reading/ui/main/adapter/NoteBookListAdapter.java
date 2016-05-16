package com.xidian.yetwish.reading.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.CommonAdapter;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemLongClickListener;
import com.xidian.yetwish.reading.framework.common_adapter.ViewHolder;
import com.xidian.yetwish.reading.framework.database.generator.NoteBook;
import com.xidian.yetwish.reading.framework.utils.BitmapUtils;
import com.xidian.yetwish.reading.framework.vo.NoteBookVo;
import com.xidian.yetwish.reading.ui.note.NoteBookActivity;

import java.util.List;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class NoteBookListAdapter extends CommonAdapter<NoteBookVo> {

    private int iconHeight;
    private int iconWidth;

    private View deleteView;

    private OnItemLongClickListener<NoteBookVo> mLongClickListener;

    public void setItemLongClickListener(OnItemLongClickListener<NoteBookVo> listener) {
        this.mLongClickListener = listener;
    }

    public NoteBookListAdapter(final Context context, List<NoteBookVo> data) {
        super(context, R.layout.item_note_book_list, data);
        setItemClickListener(new OnItemClickListener<NoteBookVo>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, NoteBookVo data, int position) {
                if (deleteView != null && deleteView.isShown()) {
                    deleteView.setVisibility(View.GONE);
                }
                NoteBookActivity.startActivity(mContext, data);
            }

            @Override
            public void onItemLongClick(ViewGroup parent, View view, NoteBookVo data, int position) {
                if(position == 0) return;
                View ivDelete = view.findViewById(R.id.ivNoteBookDelete);
                ivDelete.setVisibility(View.VISIBLE);
                deleteView = ivDelete;
                if (mLongClickListener != null)
                    mLongClickListener.onItemLongClick(ivDelete, data, position);
            }
        });
        iconHeight = mContext.getResources().getDimensionPixelSize(R.dimen.item_icon_height);
        iconWidth = mContext.getResources().getDimensionPixelSize(R.dimen.item_icon_width);
    }

    @Override
    public void convert(ViewHolder holder, NoteBookVo noteBook) {
        holder.setText(R.id.tvNoteBookName, noteBook.getName())
                .setText(R.id.tvNoteBookNum, noteBook.getNoteNumber() + "")
                .setVisible(R.id.ivNoteBookDelete,false);

        if (noteBook.getIntroduction() != null) {
            holder.setText(R.id.tvNoteBookIntro, noteBook.getIntroduction());
        }
        if (noteBook.getIconResId() != 0) {
            holder.setImageResource(R.id.ivNoteBookIcon, noteBook.getIconResId());
        } else {
            int resId = BitmapUtils.getRandomAppBarBgImageRes();
            noteBook.setIconResId(resId);
            holder.setImageBitmap(R.id.ivNoteBookIcon, BitmapUtils.decodeSampleBitmapFromResource(
                    mContext.getResources(), resId, iconWidth, iconHeight));
        }


    }


}
