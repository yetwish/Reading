package com.xidian.yetwish.reading.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventBookRead;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.ui.main.adapter.BookListAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.ui.widget.SlideMenu;
import com.xidian.yetwish.reading.ui.SlideMenuActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * read activity ,shows all the book that has been read
 */
public class ReadActivity extends SlideMenuActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReadActivity.class);
        context.startActivity(intent);
    }

    private List<BookVo> mReadList;
    private EmptyRecyclerView lvRead;

    private TextView tvEmptyHint;

    private View mEmptyView;

    private BookListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_read);
        EventBusWrapper.getDefault().register(ReadActivity.this);
        initView();
        initData();
    }

    private void initView() {
        lvRead = (EmptyRecyclerView) findViewById(R.id.lvRead);
        mEmptyView = findViewById(R.id.readEmptyView);
        tvEmptyHint = (TextView) findViewById(R.id.emptyText);
        tvEmptyHint.setText(getString(R.string.empty_read));

        LinearLayoutManager layoutManager = new LinearLayoutManager(ReadActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvRead.setLayoutManager(layoutManager);

    }

    private void initData() {
        mReadList = new ArrayList<>();
        mAdapter = new BookListAdapter(ReadActivity.this, mReadList);
        lvRead.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlideMenu.setSelectedItemIndex(SlideMenu.MENU_READ);
        refreshReadList();
    }

    private void refreshReadList() {
        ImmutableList<BookVo> readList = DatabaseManager.getsInstance().getBookManager().queryRead();
        mReadList.clear();
        mReadList.addAll(readList);
        mAdapter.notifyDataSetChanged();
        updateEmptyView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseManager.getsInstance().getBookManager().refresh(ImmutableList.copyOf(mReadList));
    }

    private void updateEmptyView() {
        if (mAdapter.getItemCount() > 0)
            mEmptyView.setVisibility(View.GONE);
        else
            mEmptyView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onBookRead(EventBookRead event) {
        if (event.getReadBook() == null) return;
        mReadList.add(event.getReadBook());
        mAdapter.notifyDataSetChanged();
        updateEmptyView();
    }
}
