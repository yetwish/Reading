package com.xidian.yetwish.reading.framework;

import android.os.Handler;
import android.os.Looper;

/**
 * 贯通framework层的helper类，之后添加图片加载器，请求列表等。
 * Created by Yetwish on 2016/4/28 0028.
 */
public class FWHelper {

    private static FWHelper sInstance;

    private Handler mMainHandler;

    public static FWHelper getInstance() {
        if (sInstance == null) {
            synchronized (FWHelper.class) {
                if (sInstance == null)
                    sInstance = new FWHelper();
            }
        }
        return sInstance;
    }

    private FWHelper() {
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }

    public Handler getMainHandler() {
        return mMainHandler;
    }
}
