package com.xidian.yetwish.reading.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xidian.yetwish.reading.R;

/**
 * splash activity
 * Created by Yetwish on 2016/4/8 0008.
 */
public class SplashActivity extends BaseActivity {

    private static final int MSG_SPLASH = 0x01;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SPLASH:
                    ReadingActivity.startActivity(SplashActivity.this);
                    finish();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    //TODO catch exception
                }finally {
                    mHandler.sendEmptyMessage(MSG_SPLASH);
                }

            }
        }).start();

    }
}
