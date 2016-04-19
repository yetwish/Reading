package com.xidian.yetwish.reading.ui.file_explorer;

import android.os.Environment;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

import java.io.File;

/**
 * Created by Yetwish on 2016/4/14 0014.
 */
public class FileInfo {

    private String fileName;
    private long size;
    private String filePath;
    private boolean isDir;
    private FileInfo parentFile;

    public FileInfo(File file) {
        this.filePath = file.getAbsolutePath();
        this.fileName = file.getName();
        this.size = file.getTotalSpace();
        this.isDir = file.isDirectory();
        if (filePath.equals(Environment.getRootDirectory()))
            this.parentFile = null;
        this.parentFile = new FileInfo(file.getParentFile());
    }

    public FileInfo(String filePath, String fileName, long size, boolean isDir) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.size = size;
        this.isDir = isDir;
    }

    public FileInfo getParentFile() {
        if (filePath.equals(Environment.getRootDirectory()))
            return null;
        return parentFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public static final Ordering<FileInfo> FILE_INFO_ORDERING = Ordering.natural().onResultOf(new Function<FileInfo, Comparable>() {
        @Override
        public Comparable apply(FileInfo input) {
            return input.isDir() ? 0 : 1;
        }
    });
}
