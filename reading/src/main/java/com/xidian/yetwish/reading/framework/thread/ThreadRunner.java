package com.xidian.yetwish.reading.framework.thread;

import android.os.Handler;
import android.os.Looper;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.xidian.yetwish.reading.framework.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
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

        mTasks = new ConcurrentHashMap<>();

        mMainHandler = new Handler(Looper.getMainLooper());

        mThreadPool = MoreExecutors.listeningDecorator(
                Executors.newFixedThreadPool(AppUtils.getNumCores()));
    }

    public synchronized <V> void start(List<? extends Callable<V>> calls, FutureCallback<List<V>> callback) {
        if (calls == null) return;
        List<ListenableFuture<V>> futureList = new ArrayList<>();
        for (int i = 0; i < calls.size(); i++) {
            Task<V> task = new Task<V>();
            task.mCallable = calls.get(i);
            task.mListenableFuture = (ListenableFuture) mThreadPool.submit(task.mCallable);
            task.mListenerWrapper = new ThreadListenerWrapper(null, task.mCallable);
            task.mListenableFuture.addListener(task.mListenerWrapper, mThreadPool);
            futureList.add(task.mListenableFuture);
            mTasks.put(task.mCallable, task);
        }
        ListenableFuture<List<V>> futures = Futures.successfulAsList(futureList);
        FutureCallbackWrapper futureCallbackWrapper = new FutureCallbackWrapper(null, callback);
        Futures.addCallback(futures, futureCallbackWrapper, mThreadPool);
    }

    /**
     * 启动一个无监听器的callable
     *
     * @param callable
     * @param <V>
     */
    public synchronized <V> void start(Callable<V> callable) {
        start(null, callable);
    }

    /**
     * 启动一个具有监听器的callable
     *
     * @param listener
     * @param callable
     * @param <V>
     */
    public synchronized <V> void start(Runnable listener, Callable<V> callable) {
        if (callable == null) return;
        Task<V> task = new Task<V>();
        task.mCallable = callable;
        task.mListenableFuture = (ListenableFuture) mThreadPool.submit(task.mCallable);
        task.mListenerWrapper = new ThreadListenerWrapper(listener, callable);
        task.mListenableFuture.addListener(task.mListenerWrapper, mThreadPool);
        mTasks.put(callable, task);
    }

    /**
     * 启动一个有回调的callable
     *
     * @param callable
     * @param callback
     * @param <V>
     */
    public synchronized <V> void start(Callable<V> callable, FutureCallback<? super V> callback) {
        if (callable == null) return;
        if (callback == null) {
            start(null, callable);
            return;
        }
        Task<V> task = new Task<V>();
        task.mCallable = callable;
        task.mListenableFuture = (ListenableFuture) mThreadPool.submit(task.mCallable);
        task.mFutureCallbackWrapper = new FutureCallbackWrapper<V>(callable, callback);
        Futures.addCallback(task.mListenableFuture, task.mFutureCallbackWrapper, mThreadPool);
        mTasks.put(callable, task);
    }

    /**
     * 取消某一任务
     *
     * @param callable
     * @param isForce
     */

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

    /**
     * 取消所有任务
     *
     * @param isForce
     */
    public synchronized void cancelAll(boolean isForce) {
        for (Callable callable : mTasks.keySet()) {
            cancel(callable, isForce);
        }
    }

    class Task<V> {
        private ListenableFuture<V> mListenableFuture;
        private Callable<V> mCallable;
        private FutureCallbackWrapper<? super V> mFutureCallbackWrapper;
        private ThreadListenerWrapper mListenerWrapper;
    }

    class ThreadListenerWrapper implements Runnable {

        private Runnable mRealListener;
        private Callable<?> mCallable;

        public ThreadListenerWrapper(Runnable runnable, Callable<?> callable) {
            this.mRealListener = runnable;
            this.mCallable = callable;
        }

        @Override
        public void run() {
            if (mRealListener != null)//线程执行完成
                mMainHandler.post(mRealListener);
            mTasks.remove(mCallable);
        }
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
                    if (mCallable != null)
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
