package com.xidian.yetwish.reading.ui.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemLongClickListener;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventAddBooks;
import com.xidian.yetwish.reading.ui.main.adapter.BookListAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.widget.SlideMenu;
import com.xidian.yetwish.reading.ui.SlideMenuActivity;
import com.xidian.yetwish.reading.ui.fab.OnFABItemClickListener;
import com.xidian.yetwish.reading.ui.fab.PopupMenu;
import com.xidian.yetwish.reading.ui.add_book.auto_search.AutoSearchActivity;
import com.xidian.yetwish.reading.ui.add_book.file_browser.FileBrowserActivity;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * main activity , use toolbar and drawerLayout to implement slideMenu
 */
public class ReadingActivity extends SlideMenuActivity {

    private PopupMenu mPopupMenu;
    private ImageButton FabReading;

    private List<BookVo> mBookList;
    private BookListAdapter mAdapter;

    private EmptyRecyclerView lvReading;
    private View mEmptyView;

    private boolean isHidden;
    private boolean isFloating;
    private float fabY;

    private View mDeleteView;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReadingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_reading);
        EventBusWrapper.getDefault().register(this);

        //初始化数据库等系统服务
        DatabaseManager.getsInstance().init(ReadingActivity.this);

        FabReading = (ImageButton) findViewById(R.id.fab_reading);
        FabReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupMenu == null) {
                    mPopupMenu = new PopupMenu(ReadingActivity.this, mFABItemClickListener);
                }
                if (!mPopupMenu.isShowing()) {
                    mPopupMenu.show(mToolbar);
                } else {
                    mPopupMenu.hide();
                }
                hideDeleteView();
            }
        });

        mEmptyView = findViewById(R.id.readingEmptyView);
        initReadingList();
    }

    private boolean hideDeleteView() {
        if (mDeleteView != null && mDeleteView.isShown()) {
            mDeleteView.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private void initReadingList() {
        mBookList = new ArrayList<>();

        mAdapter = new BookListAdapter(ReadingActivity.this, mBookList);
        mAdapter.setItemLongClickListener(new OnItemLongClickListener<BookVo>() {
            @Override
            public void onItemLongClick(final View deleteView, final BookVo data, final int position) {
                if (mDeleteView != null && mDeleteView != deleteView)
                    hideDeleteView();
                mDeleteView = deleteView;
                mDeleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBasicDialog(getString(R.string.delete_book_title) + "《" + data.getName() + "》", getString(R.string.delete_book_content),
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (which == DialogAction.POSITIVE) {
                                            //删除书籍
                                            DatabaseManager.getsInstance().getBookManager().delete(data.getBookId());
                                            //删除章节
                                            DatabaseManager.getsInstance().getChapterManager().delete(data.getBookId());
                                            //删除分页
                                            DatabaseManager.getsInstance().getPageManager().delete(data.getBookId());
                                            //删除书签
                                            DatabaseManager.getsInstance().getBookmarkManager().deleteByBookId(data.getBookId());
                                            //从内存中移除
                                            mBookList.remove(position);
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

        lvReading = (EmptyRecyclerView) findViewById(R.id.lvReading);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvReading.setLayoutManager(layoutManager);
        lvReading.setAdapter(mAdapter);

        lvReading.addOnScrollListener(
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
                "y", fabY, ScreenUtils.getScreenHeight(ReadingActivity.this));
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
                "y", ScreenUtils.getScreenHeight(ReadingActivity.this), fabY);
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


    private void updateEmptyViewVisibility() {
        if (mAdapter.getItemCount() > 0)
            mEmptyView.setVisibility(View.GONE);
        else
            mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseManager.getsInstance().getBookManager().refresh(ImmutableList.copyOf(mBookList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_READING);
        refreshBookList();
        hideDeleteView();
    }

    private void refreshBookList() {
        ImmutableList<BookVo> dbList = DatabaseManager.getsInstance().getBookManager().queryReading();
        mBookList.clear();
        mBookList.addAll(dbList);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
        updateEmptyViewVisibility();
    }

    private OnFABItemClickListener mFABItemClickListener = new OnFABItemClickListener() {

        @Override
        public void onFabItemClick(int position) {
            switch (position) {
                case PopupMenu.ITEM_TOP:
                    AutoSearchActivity.startActivity(ReadingActivity.this);
                    break;
                case PopupMenu.ITEM_BOTTOM:
                    FileBrowserActivity.startActivity(ReadingActivity.this);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(!hideDeleteView()){
            if (mPopupMenu != null && mPopupMenu.isShowing()) {
                mPopupMenu.hide();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPopupMenu = null;
    }

    @Subscribe
    public void onAddBooksToReading(EventAddBooks event) {
        List<BookVo> books = event.getBookList();
        if (books != null) {
            mBookList.addAll(books);
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
        }
        updateEmptyViewVisibility();
    }


}
