package com.xidian.yetwish.reading.ui.file_explorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.ToolbarActivity;
import com.xidian.yetwish.reading.ui.file_explorer.adapter.FileExplorerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * todo data
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

    private List<FileInfo> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_file_explorer);
        initData();
        initView();
    }

    private void initData() {
        mData = new ArrayList<FileInfo>();

        for (int i = 0; i < 10; i++) {
            mData.add(new FileInfo("Android\\", "ab" + i + ".txt", 1000 + i * 2));
            if (i % 2 == 0)
                mData.get(i).setDir(true);
        }

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
        lvFileList.setAdapter(new FileExplorerAdapter(this,mData));
    }
}
