package com.xidian.yetwish.reading.utils;


import android.util.Log;

/**
 * Log日志统一管理类
 * Created by Yetwish on 2016/4/13 0013.
 */
public class LogUtils {

    private LogUtils() {
        /* can not be instantiated */
        throw new UnsupportedOperationException("can not be instantiated");
    }

    public static boolean DEBUG = true;

    private static final String TAG = "yetwish.Reading";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (DEBUG)
            Log.d(TAG, msg);
    }

    public static void w(String msg) {
        if (DEBUG)
            Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (DEBUG)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }
}
