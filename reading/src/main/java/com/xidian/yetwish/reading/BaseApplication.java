package com.xidian.yetwish.reading;

import android.app.Application;


/**
 * base application class
 * Created by Yetwish on 2016/4/25 0025.
 */
public class BaseApplication extends Application{

    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static BaseApplication getInstance(){
        return sInstance;
    }
}
