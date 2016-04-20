package com.xidian.yetwish.reading.ui.file_explorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.thread.ThreadFactory;
import com.xidian.yetwish.reading.thread.ThreadRunner;
import com.xidian.yetwish.reading.ui.ToolbarActivity;
import com.xidian.yetwish.reading.ui.file_explorer.adapter.SearchResultAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;
import com.xidian.yetwish.reading.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * auto search activity,shows the search result.  todo size
 * Created by Yetwish on 2016/4/14 0014.
 */
public class AutoSearchActivity extends ToolbarActivity {

    private static final String ROOT_DIR = "/";

    private TXTFileFilter mFileFilter;

    private List<File> mMatchFiles;

    public static final void startActivity(Context context) {
        Intent intent = new Intent(context, AutoSearchActivity.class);
        context.startActivity(intent);
    }

    private EmptyRecyclerView lvSearchResult;
    private TextView tvAdd;
    private TextView tvEmptyView;

    private List<File> fileList;

    private SearchResultAdapter mAdapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_search);
        fileList = new ArrayList<>();
        mFileFilter = new TXTFileFilter();
        initView();
    }

    private void scanAllFiles(File rootFile) throws IOException {
        File[] files = rootFile.listFiles(mFileFilter);
        if (files == null) return;
        for (File file : files) {
            if (!file.getAbsolutePath().equals(file.getCanonicalPath()))
                continue;
            if (file.isDirectory()) {
//                LogUtils.w(file.getAbsolutePath());
                scanAllFiles(file);
            } else {
                mMatchFiles.add(file);
            }
        }
    }


    private void initView() {
        lvSearchResult = (EmptyRecyclerView) findViewById(R.id.lvSearchResult);
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvEmptyView = (TextView) findViewById(R.id.tvEmptyView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AutoSearchActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvSearchResult.setLayoutManager(layoutManager);

        lvSearchResult.setEmptyView(tvEmptyView);

        mAdapter = new SearchResultAdapter(AutoSearchActivity.this, fileList);
        lvSearchResult.setAdapter(mAdapter);

        concurrentScanFiles();

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show dialog,confirm to add books
            }
        });
    }

    /**
     * 多个线程同时遍历文件，从根文件夹开始遍历
     */
    private void concurrentScanFiles() {
        showProgressDialog();
        File rootFile = new File(ROOT_DIR);
        mMatchFiles = new ArrayList<>();
        File files[] = rootFile.listFiles(mFileFilter);
        if (files == null) return;
        List<Callable<File>> calls = new ArrayList<>();
        for (final File file : files) {
            Callable<File> call = new Callable<File>() {
                @Override
                public File call() throws Exception {
                    LogUtils.w("start scanning files!");
                    scanAllFiles(file);
                    return null;
                }
            };
            calls.add(call);
        }

        ThreadRunner.getInstance().start(calls, new FutureCallback<List<File>>() {
            @Override
            public void onSuccess(List<File> result) {
                LogUtils.w("success!");
                hideDialog();
                fileList.addAll(mMatchFiles);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.w("failure!");
                hideDialog();
                fileList.addAll(mMatchFiles);
                mAdapter.notifyDataSetChanged();

            }
        });

    }


    private void startScanFiles() {
        final File rootFile = new File(ROOT_DIR);
        mMatchFiles = new ArrayList<>();
        ThreadFactory.createThread().start(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtils.w("start scanning files!");
                    scanAllFiles(rootFile);
                } catch (IOException e) {
                    LogUtils.w("something wrong happened while scanning files!");
                    e.printStackTrace();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                LogUtils.w("finish scanning files!");
                fileList.addAll(mMatchFiles);
                mMatchFiles = null;
                mAdapter.notifyDataSetChanged();
            }
        });
    }


}