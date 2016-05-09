package com.xidian.yetwish.reading.ui.add_book.auto_search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.ui.ToolbarActivity;
import com.xidian.yetwish.reading.ui.add_book.AddBookHelper;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * auto search activity,shows the search result.  todo size
 * Created by Yetwish on 2016/4/14 0014.
 */
public class AutoSearchActivity extends ToolbarActivity {

    public static final void startActivity(Context context) {
        Intent intent = new Intent(context, AutoSearchActivity.class);
        context.startActivity(intent);
    }

    private EmptyRecyclerView lvSearchResult;
    private TextView tvAdd;
    private TextView tvEmptyView;

    private List<File> mFileList;

    private SearchResultAdapter mAdapter;

    private FileBrowser mFileBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_search);
        mFileList = new ArrayList<>();
        mFileBrowser = new FileBrowser();
        initView();
    }


    private void initView() {
        lvSearchResult = (EmptyRecyclerView) findViewById(R.id.lvSearchResult);
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvEmptyView = (TextView) findViewById(R.id.tvEmptyView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AutoSearchActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvSearchResult.setLayoutManager(layoutManager);

        lvSearchResult.setEmptyView(tvEmptyView);

        mAdapter = new SearchResultAdapter(AutoSearchActivity.this, mFileList);
        lvSearchResult.setAdapter(mAdapter);

        showProgressDialog(getString(R.string.waitForScanning), new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mFileBrowser != null)
                    mFileBrowser.cancelScanFiles();
            }
        });

        mFileBrowser.startScanFiles(new ApiCallback<ImmutableList<File>>() {
            @Override
            public void onDataReceived(ImmutableList<File> data) {
                mFileList.addAll(data);
                mAdapter.notifyDataSetChanged();
                hideProgressDialog();
            }

            @Override
            public void onException(int code, String reason) {
                hideProgressDialog();
            }
        });


        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImmutableList<File> fileList = mAdapter.getCheckFiles();
                if (fileList.size() == 0) {
                    //没选中文件
                    return;
                }
                //TODO show dialog,confirm to add books
                showProgressDialog(getString(R.string.waitForAddingBook));
                new AddBookHelper().addBook(fileList, new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        finish();
                    }
                });
            }
        });
    }



    /**
     * todo 如果是在后台，内存不足给清理了，在onPause是否需要处理？
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mFileBrowser != null)
            mFileBrowser.cancelScanFiles();
    }


}
