package com.xidian.yetwish.reading.framework.vo;

import android.graphics.Bitmap;

import com.xidian.yetwish.reading.framework.utils.BookUtils;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class BookVo {

    public static final long INIT_CHAR_NUMBER = -1;

    private String bookId;
    private String name;
    private String author;
    private String language;
    private float progress;
    private String iconPath;
    private int iconResId;
    private String filePath;
    private long charNumber;

    public BookVo(String name, String author, float progress, int resId){
        this.bookId = BookUtils.generateSequenceId();
        this.name = name;
        this.author = author;
        this.progress = progress;
        this.iconResId = resId;
        this.charNumber = INIT_CHAR_NUMBER;
    }


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getCharNumber() {
        return charNumber;
    }

    public void setCharNumber(long charNumber) {
        this.charNumber = charNumber;
    }
}
