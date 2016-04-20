package com.xidian.yetwish.reading.ui.file_explorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.ToolbarActivity;
import com.xidian.yetwish.reading.ui.file_explorer.adapter.FileExplorerAdapter;
import com.xidian.yetwish.reading.ui.widget.EmptyRecyclerView;

import java.io.File;

/**
 * todo data  todo checkbox position ,order
 * Created by Yetwish on 2016/4/14 0014.
 */
public class FileExplorerActivity extends ToolbarActivity {

    public static final void startActivity(Context context) {
        Intent intent = new Intent(context, FileExplorerActivity.class);
        context.startActivity(intent);

    }

    private TextView tvDir;
    private TextView tvAdd;
    private TextView tvEmptyView;

    private EmptyRecyclerView lvFileList;

    private FileExplorerAdapter mFileAdapter;

    Ordering<File> ordering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_file_explorer);
        initData();
        initView();
    }

    private void initData() {
        //放在FileInfo中
        ordering = Ordering.natural().onResultOf(new Function<File, Integer>() {
            @Override
            public Integer apply(File input) {
                return input.isDirectory() ? 0 : 1;
            }
        });
    }

    private void initView() {
        tvDir = (TextView) findViewById(R.id.tvDir);
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvEmptyView = (TextView) findViewById(R.id.tvEmptyView);
        lvFileList = (EmptyRecyclerView) findViewById(R.id.lvFileList);

        lvFileList.setEmptyView(tvEmptyView);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show dialog,confirm to add books. repeat code @AutoSearchActivity#tvAdd.
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvFileList.setLayoutManager(layoutManager);

        mFileAdapter = new FileExplorerAdapter(this, Environment.getExternalStorageDirectory());

        mFileAdapter.setFilePathChangedListener(new FileExplorerAdapter.OnFilePathChangedListener() {
            @Override
            public void onFilePathChanged(String path) {
                tvDir.setText(path);
            }
        });
        lvFileList.setAdapter(mFileAdapter);
    }
}
