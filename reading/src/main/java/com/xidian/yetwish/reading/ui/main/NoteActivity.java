package com.xidian.yetwish.reading.ui.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.vo.NoteBookVo;
import com.xidian.yetwish.reading.ui.fab.OnFABItemClickListener;
import com.xidian.yetwish.reading.ui.fab.PopupMenu;
import com.xidian.yetwish.reading.ui.main.adapter.BookListAdapter;
import com.xidian.yetwish.reading.ui.main.adapter.NoteBookListAdapter;
import com.xidian.yetwish.reading.ui.note.NoteEditActivity;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.widget.SlideMenu;
import com.xidian.yetwish.reading.ui.SlideMenuActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * note activity , shows various notes of books
 */
public class NoteActivity extends SlideMenuActivity {

    private ImageButton FabReading;

    private List<NoteBookVo> mNoteBookList = new ArrayList<>();
    private NoteBookListAdapter mAdapter;

    private EmptyRecyclerView lvNoteBook;

    private boolean isHidden;
    private boolean isFloating;
    private float fabY;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NoteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_note);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_NOTE);
        refreshNoteList();
    }

    private void initView() {
        FabReading = (ImageButton) findViewById(R.id.fab_note);
        FabReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建新的日记本
            }
        });

        lvNoteBook = (EmptyRecyclerView) findViewById(R.id.lvNoteBook);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvNoteBook.setLayoutManager(layoutManager);

        mAdapter = new NoteBookListAdapter(NoteActivity.this,mNoteBookList);
        lvNoteBook.setAdapter(mAdapter);

        lvNoteBook.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (!isFloating && !isHidden && dy > 0) {
                            hideFAB();
                        } else if (!isFloating && isHidden && dy < 0) {
                            showFAB();
                        }
                    }
                });
    }


    /**
     * 隐藏fab
     */
    private void hideFAB() {
        isHidden = true;
        fabY = FabReading.getY();
        ObjectAnimator hideAnimator = new ObjectAnimator().ofFloat(FabReading,
                "y", fabY, ScreenUtils.getScreenHeight(NoteActivity.this));
        hideAnimator.setDuration(200);
        hideAnimator.addListener(mAnimationListener);
        hideAnimator.start();
    }

    /**
     * 显示fab
     */
    private void showFAB() {
        isHidden = false;
        ObjectAnimator showAnimator = new ObjectAnimator().ofFloat(FabReading,
                "y", ScreenUtils.getScreenHeight(NoteActivity.this), fabY);
        showAnimator.setDuration(200);
        showAnimator.addListener(mAnimationListener);
        showAnimator.start();
    }

    private Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isFloating = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isFloating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    private void refreshNoteList() {
        ImmutableList<NoteBookVo> dbList = DatabaseManager.getsInstance().getNoteBookManager().queryAll();
        mNoteBookList.clear();
        LogUtils.w(dbList.size()+"..");
        mNoteBookList.addAll(dbList);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }


}
