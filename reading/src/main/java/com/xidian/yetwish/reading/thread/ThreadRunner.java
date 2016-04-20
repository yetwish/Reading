package com.xidian.yetwish.reading.thread;

import android.os.Handler;
import android.os.Looper;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.xidian.yetwish.reading.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * thread pool, use mainHandler post the result to ui thread
 * Created by Yetwish on 2016/4/19 0019.
 */
public class ThreadRunner {

    private ExecutorService mThreadPool;

    private Handler mMainHandler;


    private Map<Callable<?>, Task> mTasks;

    private ThreadRunner() {
        initThreadPool();
    }

    /**
     * singleton
     */
    private static class ThreadServiceHolder {
        private static final ThreadRunner instance = new ThreadRunner();
    }

    public static ThreadRunner getInstance() {
        return ThreadServiceHolder.instance;
    }

    private void initThreadPool() {

        mTasks = new HashMap<>();

        mMainHandler = new Handler(Looper.getMainLooper());

        mThreadPool = MoreExecutors.listeningDecorator(
                Executors.newFixedThreadPool(AppUtils.getNumCores()));
    }

    public synchronized <V> void start(Callable<V> callable, FutureCallback<? super V> callback) {
        if (callable == null) return;
        Task<V> task = new Task<V>();
        task.mCallable = callable;
        task.mFutureCallbackWrapper = new FutureCallbackWrapper<V>(callable, callback);
        task.mListenableFuture = (ListenableFuture) mThreadPool.submit(task.mCallable);
        Futures.addCallback(task.mListenableFuture, task.mFutureCallbackWrapper, mThreadPool);
        mTasks.put(callable, task);
    }

    public synchronized void cancel(Callable<?> callable, boolean isForce) {
        Task task = mTasks.get(callable);
        if (task == null)
            return;
        if (task.mListenableFuture != null) {
            if (!task.mListenableFuture.cancel(isForce))
                return;
        }
        mTasks.remove(callable);
    }


    class Task<V> {
        private ListenableFuture<V> mListenableFuture;
        private Callable<V> mCallable;
        private FutureCallbackWrapper<? super V> mFutureCallbackWrapper;
    }


    class FutureCallbackWrapper<V> implements FutureCallback<V> {

        private Callable<V> mCallable;
        private FutureCallback<? super V> mRealFutureCallback;

        public FutureCallbackWrapper(Callable<V> callable, FutureCallback<? super V> callback) {
            this.mCallable = callable;
            this.mRealFutureCallback = callback;
        }

        @Override
        public void onSuccess(final V result) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRealFutureCallback == null) return;
                    mRealFutureCallback.onSuccess(result);
                    mTasks.remove(mCallable);
                }
            });
        }

        @Override
        public void onFailure(final Throwable t) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    //TODO remove or not
                    mRealFutureCallback.onFailure(t);
                    mTasks.remove(mCallable);
                }
            });
        }
    }
}
