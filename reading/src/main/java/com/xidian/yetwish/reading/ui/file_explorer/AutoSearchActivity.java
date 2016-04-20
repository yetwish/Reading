package com.xidian.yetwish.reading.ui.file_explorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.thread.ThreadFactory;
import com.xidian.yetwish.reading.ui.ToolbarActivity;
import com.xidian.yetwish.reading.ui.file_explorer.adapter.SearchResultAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * auto search activity,shows the search result.
 * Created by Yetwish on 2016/4/14 0014.
 */
public class AutoSearchActivity extends ToolbarActivity {

    public static final void startActivity(Context context) {
        Intent intent = new Intent(context, AutoSearchActivity.class);
        context.startActivity(intent);
    }

    private RecyclerView lvSearchResult;
    private TextView tvAdd;

    private List<File> fileList;

    private SearchResultAdapter mAdapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            File file = (File) msg.obj;
            fileList.add(0, file);
            mAdapter.notifyDataSetChanged();
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_search);
        initData();
        initView();
    }

    private void initData() {

        fileList = new ArrayList<>();
        mAdapter = new SearchResultAdapter(AutoSearchActivity.this, R.layout.item_file_list, fileList);

        Collections.addAll(fileList, Environment.getRootDirectory().listFiles());

        ThreadFactory.createThread().start(new Callable<File>() {
            @Override
            public File call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                return new File(Environment.getRootDirectory().getPath(), "name");
            }
        }, new FutureCallback<File>() {
            @Override
            public void onSuccess(File result) {
                fileList.add(0, result);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }


    private void initView() {
        lvSearchResult = (RecyclerView) findViewById(R.id.lvSearchResult);
        tvAdd = (TextView) findViewById(R.id.tvAdd);

        LinearLayoutManager layoutManager = new LinearLayoutManager(AutoSearchActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvSearchResult.setLayoutManager(layoutManager);

        lvSearchResult.setAdapter(mAdapter);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show dialog,confirm to add books
            }
        });
    }

}
