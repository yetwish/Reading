package com.xidian.yetwish.reading.ui.file_explorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.ToolbarActivity;
import com.xidian.yetwish.reading.ui.file_explorer.adapter.SearchResultAdapter;

import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainLayout(R.layout.activity_search);
        initData();
        initView();
    }

    private void initData() {
        fileList = new ArrayList<>();
        Collections.addAll(fileList, Environment.getRootDirectory().listFiles());
    }

    private void initView() {
        lvSearchResult = (RecyclerView) findViewById(R.id.lvSearchResult);
        tvAdd = (TextView) findViewById(R.id.tvAdd);

        LinearLayoutManager layoutManager = new LinearLayoutManager(AutoSearchActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvSearchResult.setLayoutManager(layoutManager);

        lvSearchResult.setAdapter(new SearchResultAdapter(AutoSearchActivity.this, R.layout.item_file_list, fileList));

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show dialog,confirm to add books
            }
        });
    }

}
