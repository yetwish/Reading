package com.xidian.yetwish.reading.ui.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.NoteBook;
import com.xidian.yetwish.reading.framework.exception.IllegalIntentDataException;
import com.xidian.yetwish.reading.framework.utils.BitmapUtils;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.SharedPreferencesUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.vo.NoteBookVo;
import com.xidian.yetwish.reading.framework.vo.NoteVo;
import com.xidian.yetwish.reading.ui.BaseActivity;
import com.xidian.yetwish.reading.ui.com.baobomb.popuplistview.PopupListView;
import com.xidian.yetwish.reading.ui.com.baobomb.popuplistview.PopupView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yetwish on 2016/5/9 0009.
 */
public class NoteBookActivity extends BaseActivity implements PopupListView.PopupStatusChangeListener {

    public static void startActivity(Context context, NoteBookVo data) {
        Intent intent = new Intent(context, NoteBookActivity.class);
        intent.putExtra(SharedPreferencesUtils.EXTRA_NOTEBOOK, data);
        context.startActivity(intent);
    }

    private NoteBookVo mNoteBook;
    private List<NoteVo> mNoteList;

    private PopupListView lvNote;
    private View mEmptyView;
    private List<PopupView> mNoteViews;

    private AppBarLayout mAppBarLayout;
    private NestedScrollView mScrollView;

    private FloatingActionButton mFabAddNote;

    private LinearLayout.LayoutParams mEmptyViewLayoutParams;

    private boolean hasMenu = false;

    private boolean isPopup = false;

    private int popupItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebook_activity);
        initData();
        initView();
    }

    private void initData() {
        Serializable data = getIntent().getSerializableExtra(SharedPreferencesUtils.EXTRA_NOTEBOOK);
        if (!(data instanceof NoteBookVo))
            throw new IllegalIntentDataException(NoteBookVo.class, data.getClass());
        mNoteBook = (NoteBookVo) data;
        mNoteList = new ArrayList<>();
    }

    private void initView() {
        lvNote = (PopupListView) findViewById(R.id.lvNote);
        lvNote.init(null);
        mNoteViews = new ArrayList<>();
        lvNote.setItemViews(mNoteViews);
        lvNote.setPopupItemClickListener(this);

        mEmptyView = findViewById(R.id.noteEmptyView);
        mEmptyViewLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mNoteBook.getName());


        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        final int width = ScreenUtils.getScreenWidth(NoteBookActivity.this);
        final int height = getResources().getDimensionPixelSize(R.dimen.app_bar_backdrop_height);
        if (imageView != null)
            imageView.setImageBitmap(BitmapUtils.decodeSampleBitmapFromResource(
                    getResources(), mNoteBook.getIconResId(), width, height));
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mScrollView = (NestedScrollView) findViewById(R.id.noteScrollContainer);
        mFabAddNote = (FloatingActionButton) findViewById(R.id.fabAddNote);

        final int screenHeight = ScreenUtils.getScreenHeight(NoteBookActivity.this);
        final int statusHeight = ScreenUtils.getStatusHeight(NoteBookActivity.this);
        final int margin = getResources().getDimensionPixelSize(R.dimen.note_app_bar_margin);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mEmptyView.isShown()) {
                    mEmptyViewLayoutParams = (LinearLayout.LayoutParams) mEmptyView.getLayoutParams();
                    mEmptyViewLayoutParams.height = screenHeight - statusHeight - height - verticalOffset - margin;
                    mEmptyView.setLayoutParams(mEmptyViewLayoutParams);
                }
                if (mFabAddNote.isShown() && hasMenu) {
                    hasMenu = false;
                    invalidateOptionsMenu();
                } else if (!mFabAddNote.isShown()) {
                    hasMenu = true;
                    invalidateOptionsMenu();
                }

            }
        });

        mFabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteEditActivity.startActivity(NoteBookActivity.this, mNoteBook.getNoteBookId());
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (hasMenu) {
            menu.findItem(R.id.menu_note_add).setVisible(true);
        } else {
            menu.findItem(R.id.menu_note_add).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_note_add:
                if (isPopup) {//编辑
                    NoteEditActivity.startActivityForResult(NoteBookActivity.this, mNoteList.get(popupItemPosition));
                } else {
                    NoteEditActivity.startActivity(NoteBookActivity.this, mNoteBook.getNoteBookId());
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SharedPreferencesUtils.REQUEST_CODE_NOTE_EDIT) {
            if (resultCode == RESULT_OK) {
                //更新popupView
                mNoteList.set(popupItemPosition,
                        (NoteVo) data.getSerializableExtra(SharedPreferencesUtils.EXTRA_NOTE));
                lvNote.refresh();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_add, menu);
        return true;
    }

    private void refreshNoteList() {
        ImmutableList<NoteVo> noteList = DatabaseManager.getsInstance()
                .getNoteManager().queryByNoteBook(mNoteBook.getNoteBookId());
        if (noteList.size() == 0) {
            lvNote.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            lvNote.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mNoteList.clear();
            mNoteList.addAll(noteList);
            mNoteViews.clear();
            for (int i = 0; i < mNoteList.size(); i++) {
                PopupView<NoteVo> popupView = new PopupView<NoteVo>(this, R.layout.item_note_list, mNoteList.get(i)) {
                    @Override
                    public void setItemView(View view, NoteVo data) {
                        final TextView tv = (TextView) view.findViewById(R.id.tvNoteItemTitle);
                        tv.setText(data.getName());
                    }

                    @Override
                    public View setExtendView(View view, NoteVo data) {
                        View extendView;
                        if (view == null) {
                            extendView = LayoutInflater.from(NoteBookActivity.this).inflate(
                                    R.layout.note_detail, null);
                            EditText et = (EditText) extendView.findViewById(R.id.etNoteContent);
                            et.setText(data.getContent());
                            et.setEnabled(false);
                        } else {
                            extendView = view;
                        }
                        return extendView;
                    }
                };
                mNoteViews.add(popupView);
            }
            lvNote.refresh();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mNoteViews.size() != mNoteBook.getNoteNumber()) {
            mNoteBook.setNoteNumber(mNoteViews.size());
            DatabaseManager.getsInstance().getNoteBookManager().refresh(mNoteBook);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNoteList();
    }

    @Override
    public void onBackPressed() {
        if (lvNote.isItemZoomIn()) {
            lvNote.zoomOut();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPopup(int position) {
        if (mAppBarLayout == null || mScrollView == null) return;
        mAppBarLayout.setExpanded(false);
        mScrollView.scrollTo(0, 0);
        isPopup = true;
        popupItemPosition = position;
    }

    @Override
    public void onCollapse() {
        if (mScrollView == null) return;
        mScrollView.scrollTo(0, 0);
        isPopup = false;
    }
}
