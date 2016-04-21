package com.xidian.yetwish.reading.framework.thread;

import com.google.common.util.concurrent.FutureCallback;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * sub thread
 * Created by Yetwish on 2016/4/19 0019.
 */
public class BingoThread {

    private boolean isStarted = false;

    private Callable<?> mCallable;

    BingoThread() {
    }

    /**
     * 启动一个不需要知道什么时候执行完成的线程
     * @param runnable
     */
    public void start(Runnable runnable) {
        start(runnable, null);
    }

    /**
     * 启动一个不需要获取执行结果的线程,callback 在线程执行完成时调用该runnable
     *
     * @param runnable
     */
    public void start(Runnable runnable, Runnable listener) {
        if (!isStarted) {
            mCallable = Executors.callable(runnable);
            ThreadRunner.getInstance().start(listener, mCallable);
            isStarted = true;
        }
    }


    /**
     * 启动一个需要获取执行结果的线程。通过callback回调接口获取结果
     *
     * @param callable
     * @param callback
     * @param <V>
     */
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
