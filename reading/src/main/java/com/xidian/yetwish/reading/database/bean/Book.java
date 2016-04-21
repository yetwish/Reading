package com.xidian.yetwish.reading.database.bean;

import android.graphics.Bitmap;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class Book {
    private int bookId;
    private String name;
    private String author;
    private String language;
    private int progress;
    private Bitmap icon;
    private int iconResId;
    private String filePath;

    public Book(int bookId,String name,String author,int progress,int resId){
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.progress = progress;
        this.iconResId = resId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
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
}
