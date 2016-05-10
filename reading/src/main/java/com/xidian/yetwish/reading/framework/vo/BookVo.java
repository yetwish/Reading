package com.xidian.yetwish.reading.framework.vo;


import com.google.common.io.Files;
import com.xidian.yetwish.reading.framework.database.generator.Book;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class BookVo implements Serializable {

    private long bookId;
    private String name;
    private String author;
    private String language;
    private float progress;
    private String iconPath;
    private int iconResId;
    private String filePath;
    private long charNumber;

    public BookVo(Book book) {
        this.bookId = book.getBookId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.language = book.getLanguage();
        this.progress = book.getProgress();
        this.iconPath = book.getIconPath();
        this.filePath = book.getPath();
        this.charNumber = book.getCharNumber();
        this.iconResId = book.getIconResId();
    }

    public Book convertToDb() {
        Book book = new Book();
        book.setBookId(bookId);
        book.setName(name);
        book.setAuthor(author);
        book.setLanguage(language);
        book.setProgress(progress);
        book.setIconPath(iconPath);
        book.setPath(filePath);
        book.setCharNumber(charNumber);
        book.setIconResId(iconResId);
        return book;
    }

    public BookVo(String filePath, String name) {
        this.bookId = BookUtils.generateSequenceId();
        this.filePath = filePath;
        this.name = Files.getNameWithoutExtension(name);
        this.progress = 0;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
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
