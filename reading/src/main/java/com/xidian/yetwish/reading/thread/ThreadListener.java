package com.xidian.yetwish.reading.thread;

import java.util.concurrent.Callable;

/**
 * Created by Yetwish on 2016/4/20 0020.
 */
public class ThreadListener implements Runnable {

    private OnThreadFinishListener listener;

    public ThreadListener(OnThreadFinishListener listener){
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.onThreadFinish();
    }

}
