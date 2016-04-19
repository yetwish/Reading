package com.xidian.yetwish.reading.ui.file_explorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.ToolbarActivity;
import com.xidian.yetwish.reading.ui.file_explorer.adapter.FileExplorerAdapter;
import com.xidian.yetwish.reading.ui.file_explorer.adapter.FileExplorerAdapter2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private RecyclerView lvFileList;

    private File[] files;
    private List<File> mFileList;

    private List<FileInfo> mData;

    Ordering<FileInfo> ordering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_file_explorer);
        initData();
        initView();
    }

    private void initData() {

        files = Environment.getRootDirectory().listFiles();
        mFileList = new ArrayList<File>();
        Collections.addAll(mFileList,files);
        mData = new ArrayList<FileInfo>();

        for (int i = 0; i < 10; i++) {
            mData.add(new FileInfo("Android\\", "ab" + i + ".txt", 1000 + i * 2));
            if (i % 2 == 0)
                mData.get(i).setDir(true);
        }
        mData.add(new FileInfo("Android\\", "aaaa"  + ".txt", 908));
        mData.get(mData.size()-1).setDir(false);
        //放在FileInfo中
        ordering = Ordering.natural().onResultOf(new Function<FileInfo, Integer>() {
            @Override
            public Integer apply(FileInfo input) {
                return input.isDir() ? 0 : 1;
            }
        });

    }

    private void initView() {
        tvDir = (TextView) findViewById(R.id.tvDir);
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        lvFileList = (RecyclerView) findViewById(R.id.lvFileList);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show dialog,confirm to add books. repeat code @AutoSearchActivity#tvAdd.
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvFileList.setLayoutManager(layoutManager);
        lvFileList.setAdapter(new FileExplorerAdapter2(this,mFileList));
    }
}
