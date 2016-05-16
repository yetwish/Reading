package com.xidian.yetwish.reading.ui.add_book.auto_search;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.thread.ThreadFactory;
import com.xidian.yetwish.reading.framework.thread.ThreadRunner;
import com.xidian.yetwish.reading.framework.utils.FileUtils;
import com.xidian.yetwish.reading.ui.add_book.TXTFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * todo 排序
 * Created by Yetwish on 2016/5/9 0009.
 */
public class FileBrowser {

    private TXTFileFilter mFileFilter;

    private List<File> mMatchFiles;

    private int _threadCount;
    private volatile int _threadFinishCount;

    private ApiCallback<ImmutableList<File>> mCallback;

    FileBrowser() {
        mMatchFiles = new ArrayList<>();
        mFileFilter = new TXTFileFilter();
    }


    /**
     * 多个线程同时遍历文件，从根文件夹开始遍历
     */
    public void startScanFiles(ApiCallback<ImmutableList<File>> callback) {
        mCallback = callback;
        File rootFile = new File(FileUtils.getStorageDir());
        File files[] = rootFile.listFiles(mFileFilter);
        if (files == null) {
            mCallback.onDataReceived(null);
            return;
        }
        _threadCount = files.length;
        for (final File file : files) {
            ThreadFactory.createThread().start(new Runnable() {
                @Override
                public void run() {
                    try {
                        scanAllFiles(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, mThreadListener);
        }
    }

    private Runnable mThreadListener = new Runnable() {
        @Override
        public void run() {
            synchronized (FileBrowser.this) {
                _threadFinishCount++;
                if (_threadFinishCount == _threadCount) {
                    //扫描完成
//                    mFileList.addAll(mMatchFiles);
                    if (mCallback != null) {
                        mCallback.onDataReceived(FileUtils.ORDERING_FILE.immutableSortedCopy(mMatchFiles));
                    }
                }
            }
        }
    };


    private void scanAllFiles(File rootFile) throws IOException {
        File[] files = rootFile.listFiles(mFileFilter);
        if (files == null) return;
        for (File file : files) {
            if (!file.getAbsolutePath().equals(file.getCanonicalPath()))
                continue;
            if (file.isDirectory()) {
//                LogUtils.w(file.getAbsolutePath());
                scanAllFiles(file);
            } else {
                if (file.length() > 1024) //过滤掉<1KB的文件
                    mMatchFiles.add(file);
            }
        }
    }

    public void cancelScanFiles() {
        ThreadRunner.getInstance().cancelAll(true);
    }

}
