package com.xidian.yetwish.reading.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.xidian.yetwish.reading.R;

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar ;


    public static void startActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.mipmap.ic_menu_white_24dp);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        //TODO necessary?
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
