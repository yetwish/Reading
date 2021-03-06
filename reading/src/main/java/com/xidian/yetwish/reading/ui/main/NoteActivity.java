package com.xidian.yetwish.reading.ui.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemLongClickListener;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.utils.FileUtils;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.utils.ToastUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.NoteBookVo;
import com.xidian.yetwish.reading.ui.main.adapter.NoteBookListAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.widget.SlideMenu;
import com.xidian.yetwish.reading.ui.SlideMenuActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * note activity , shows various notes of books
 */
public class NoteActivity extends SlideMenuActivity {

    private ImageButton fabAddNoteBook;

    private List<NoteBookVo> mNoteBookList = new ArrayList<>();
    private NoteBookListAdapter mAdapter;

    private EmptyRecyclerView lvNoteBook;

    private boolean isHidden;
    private boolean isFloating;
    private float fabY;

    private View mDeleteView;

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

        fabAddNoteBook = (ImageButton) findViewById(R.id.fabAddNoteBook);
        fabAddNoteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDeleteView();
                //创建新的日记本
                showInputDialog(getString(R.string.create_new_note_book), getString(R.string.hint_note_book_name),
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                if (TextUtils.isEmpty(input.toString().trim())) {
                                    ToastUtils.showShort(NoteActivity.this, getString(R.string.toast_empty_name));
                                } else {
                                    NoteBookVo noteBook = new NoteBookVo(input.toString().trim());
                                    mNoteBookList.add(noteBook);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        });

        lvNoteBook = (EmptyRecyclerView) findViewById(R.id.lvNoteBook);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvNoteBook.setLayoutManager(layoutManager);

        mAdapter = new NoteBookListAdapter(NoteActivity.this, mNoteBookList);
        mAdapter.setItemLongClickListener(new OnItemLongClickListener<NoteBookVo>() {
            @Override
            public void onItemLongClick(final View deleteView, final NoteBookVo data, final int position) {
                if (mDeleteView != null && mDeleteView != deleteView)
                    hideDeleteView();
                mDeleteView = deleteView;
                mDeleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBasicDialog(getString(R.string.delete_notebook_title) + "-" + data.getName(), getString(R.string.delete_notebook_content),
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (which == DialogAction.POSITIVE) {
                                            //删除笔记本
                                            DatabaseManager.getsInstance().getNoteBookManager().delete(data.getNoteBookId());
                                            //删除笔记
                                            DatabaseManager.getsInstance().getNoteManager().deleteByNoteBookId(data.getNoteBookId());
                                            //删除本地数据
                                            FileUtils.deleteNoteBooK(data.getNoteBookId());
                                            //从内存中移除
                                            mNoteBookList.remove(position);
                                            mAdapter.notifyDataSetChanged();
                                        } else if (which == DialogAction.NEGATIVE) {
                                            //取消删除
                                            hideDeleteView();
                                            mDeleteView = null;
                                        }
                                    }
                                });
                    }
                });
            }
        });

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
        fabY = fabAddNoteBook.getY();
        ObjectAnimator hideAnimator = new ObjectAnimator().ofFloat(fabAddNoteBook,
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
        ObjectAnimator showAnimator = new ObjectAnimator().ofFloat(fabAddNoteBook,
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
        hideDeleteView();
        ImmutableList<NoteBookVo> dbList = DatabaseManager.getsInstance().getNoteBookManager().queryAll();
        mNoteBookList.clear();
        mNoteBookList.addAll(dbList);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    private boolean hideDeleteView() {
        if (mDeleteView != null && mDeleteView.isShown()) {
            mDeleteView.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!hideDeleteView()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保存noteBookList
        DatabaseManager.getsInstance().getNoteBookManager().refresh(ImmutableList.copyOf(mNoteBookList));
    }
}
