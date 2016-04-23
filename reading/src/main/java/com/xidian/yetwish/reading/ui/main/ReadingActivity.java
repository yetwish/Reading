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

import com.google.common.eventbus.Subscribe;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.vo.Book;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventAddBooks;
import com.xidian.yetwish.reading.ui.main.adapter.BookListAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.widget.SlideMenu;
import com.xidian.yetwish.reading.ui.SlideMenuActivity;
import com.xidian.yetwish.reading.ui.fab.OnFABItemClickListener;
import com.xidian.yetwish.reading.ui.fab.PopupMenu;
import com.xidian.yetwish.reading.ui.file_browser.AutoSearchActivity;
import com.xidian.yetwish.reading.ui.file_browser.FileBrowserActivity;
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

    private List<Book> mBookList;
    private BookListAdapter mAdapter;

    private EmptyRecyclerView lvReading;

    private boolean isHidden;
    private boolean isFloating;
    private float fabY;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReadingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_reading);
        LogUtils.w("receive create");
//        EventBus.getDefault().register(this);
        EventBusWrapper.getDefault().register(this);
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
            }
        });

        initReadingList();
    }

    private void initReadingList() {
        mBookList = new ArrayList<>();
        mBookList.add(new Book(1, "Thinking in Java", "Bruce Eckel", 30, R.mipmap.thinking_in_java));
        mBookList.add(new Book(2, "Le Petit Prince", "[法] 圣埃克苏佩里", 96, R.mipmap.book_icon));
        mBookList.add(new Book(1, "Thinking in Java", "Bruce Eckel", 30, R.mipmap.thinking_in_java));
        mBookList.add(new Book(2, "Le Petit Prince", "[法] 圣埃克苏佩里", 96, R.mipmap.book_icon));
        mBookList.add(new Book(2, "Le Petit Prince", "[法] 圣埃克苏佩里", 96, R.mipmap.book_icon));
        mBookList.add(new Book(2, "Le Petit Prince", "[法] 圣埃克苏佩里", 96, R.mipmap.book_icon));
        mAdapter = new BookListAdapter(ReadingActivity.this, mBookList);
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


    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_READING);
        LogUtils.w("receive onResume" + ", " + mBookList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.w("receive start");
    }

    private OnFABItemClickListener mFABItemClickListener = new OnFABItemClickListener() {
        @Override
        public void onAutoSearching() {
            AutoSearchActivity.startActivity(ReadingActivity.this);
        }

        @Override
        public void onChoseManually() {
            FileBrowserActivity.startActivity(ReadingActivity.this);
        }
    };


    @Subscribe
    public void onBookAdded(EventAddBooks event) {
        List<Book> books = event.getBookList();
        LogUtils.w("add books" + ", " + books);
        if (books != null) {
            mBookList.addAll(books);
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
        }
    }

}
