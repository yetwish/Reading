package com.xidian.yetwish.reading.ui.file_explorer;


/**
 *
 * Created by Yetwish on 2016/4/14 0014.
 */
public class FileInfo {

    private String fileName;
    private long size;
    private String filePath;
    private boolean isDir;

    public FileInfo(String filePath ,String fileName,long size){
        this.filePath = filePath;
        this.fileName = fileName;
        this.size = size;
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
}
