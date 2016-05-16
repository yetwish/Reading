package com.xidian.yetwish.reading.ui.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemLongClickListener;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.exception.IllegalIntentDataException;
import com.xidian.yetwish.reading.framework.utils.BitmapUtils;
import com.xidian.yetwish.reading.framework.utils.FileUtils;
import com.xidian.yetwish.reading.framework.utils.SharedPreferencesUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.vo.NoteBookVo;
import com.xidian.yetwish.reading.framework.vo.NoteVo;
import com.xidian.yetwish.reading.ui.BaseActivity;
import com.xidian.yetwish.reading.ui.widget.popuplistview.PopupListView;
import com.xidian.yetwish.reading.ui.widget.popuplistview.PopupView;

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

    private TextView tvNoteBookIntro;

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
        lvNote.setItemLongClickListener(new OnItemLongClickListener<NoteVo>() {
            @Override
            public void onItemLongClick(View deleteView, NoteVo data, final int position) {
                final NoteVo note = mNoteList.get(position);
                showBasicDialog(getString(R.string.delete_note_title), note.getName(),
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (which == DialogAction.POSITIVE) {
                                    //删除
                                    DatabaseManager.getsInstance().getNoteManager().delete(note.getNoteId());
                                    FileUtils.deleteNote(note);
                                    mNoteList.remove(position);
                                    mNoteViews.remove(position);
                                    lvNote.refresh();
                                    refreshEmptyViewVisibility();
                                }
                            }
                        });
            }
        });

        tvNoteBookIntro = (TextView) findViewById(R.id.tvNoteBookIntro);
        if (mNoteBook.getIntroduction() != null)
            tvNoteBookIntro.setText(mNoteBook.getIntroduction());
        tvNoteBookIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                showInputDialog(mNoteBook.getName(), getString(R.string.hint_note_book_intro),
                        tvNoteBookIntro.getText(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                tvNoteBookIntro.setText(input);
                                mNoteBook.setIntroduction(input.toString());
                            }
                        });
            }
        });

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
                if (lvNote.isItemZoomIn()) {
                    lvNote.zoomOut();
                } else finish();
            }
        });
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mNoteBook.getName());


        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        final int width = ScreenUtils.getScreenWidth(NoteBookActivity.this);
        final int height = getResources().getDimensionPixelSize(R.dimen.app_bar_backdrop_height);
        if (imageView != null) {
            Bitmap bitmap = BitmapUtils.decodeSampleBitmapFromResource(
                    getResources(), mNoteBook.getIconResId(), width, height);
            imageView.setImageBitmap(bitmap);
//            BitmapUtils.loadMutedColorForBitmap(bitmap, new ApiCallback<Integer>() {
//                @Override
//                public void onDataReceived(Integer data) {
//                    //只在android 5.0以上才有效果 todo 没效果
//                    collapsingToolbar.setStatusBarScrimColor(data);
//                }
//
//                @Override
//                public void onException(int code, String reason) {
//                }
//            });
        }

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
                    if (mEmptyViewLayoutParams == null)
                        mEmptyViewLayoutParams = (LinearLayout.LayoutParams) mEmptyView.getLayoutParams();
                    mEmptyViewLayoutParams.height = screenHeight - statusHeight - height - verticalOffset - margin;
                    mEmptyView.setLayoutParams(mEmptyViewLayoutParams);
                }
                if (mFabAddNote.isShown() && hasMenu) {
                    hasMenu = false;
                    invalidateOptionsMenu();
                } else if (!mFabAddNote.isShown() && !hasMenu) {
                    hasMenu = true;
                    invalidateOptionsMenu();
                }

            }
        });

        mFabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPopup) {//编辑
                    NoteEditActivity.startActivityForResult(NoteBookActivity.this, mNoteList.get(popupItemPosition));
                } else {
                    NoteEditActivity.startActivity(NoteBookActivity.this, mNoteBook.getNoteBookId());
                }
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

    private void refreshEmptyViewVisibility() {
        if (mNoteList.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            lvNote.setVisibility(View.GONE);
        } else {
            lvNote.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void refreshNoteList() {
        ImmutableList<NoteVo> noteList = DatabaseManager.getsInstance()
                .getNoteManager().queryByNoteBook(mNoteBook.getNoteBookId());
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

        refreshEmptyViewVisibility();

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mNoteViews.size() != mNoteBook.getNoteNumber()) {
            mNoteBook.setNoteNumber(mNoteViews.size());
        }
        DatabaseManager.getsInstance().getNoteBookManager().refresh(mNoteBook);
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
