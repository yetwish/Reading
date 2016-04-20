package com.xidian.yetwish.reading.thread;

import com.google.common.util.concurrent.FutureCallback;

import java.util.concurrent.Callable;

/**
 * sub thread
 * Created by Yetwish on 2016/4/19 0019.
 */
public class BingoThread {

    private boolean isStarted = false;

    private Callable<?> mCallable;

    BingoThread() {
    }

    public <V> void start(Callable<V> callable, FutureCallback callback) {
        if (!isStarted) {
            mCallable = callable;
            ThreadRunner.getInstance().start(mCallable, callback);
            isStarted = true;
        }

    }

    public void cancel(boolean mayInterruptIfRunning) {
        ThreadRunner.getInstance().cancel(mCallable, mayInterruptIfRunning);
    }
}
