package com.xidian.yetwish.reading.framework.vo.reader;

import com.xidian.yetwish.reading.framework.database.generator.Bookmark;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

/**
 * Bookmark(书签)value object
 * Created by Yetwish on 2016/5/12 0012.
 */
public class BookmarkVo {

    private long bookId;
    private long lastCharPosition;
    private long bookmarkId;
    private String name;
    private int chapterIndex;

    public BookmarkVo(long bookId, long lastCharPosition,String name,int chapterIndex) {
        this.bookmarkId = BookUtils.generateSequenceId();
        this.bookId = bookId;
        this.lastCharPosition= lastCharPosition;
        this.name =  name;
        this.chapterIndex = chapterIndex;
    }

    public BookmarkVo(Bookmark bookmark) {
        this.bookId = bookmark.getBookId();
        this.bookmarkId = bookmark.getBookmarkId();
        this.lastCharPosition = bookmark.getLastCharPosition();
        this.name = bookmark.getName();
        this.chapterIndex = bookmark.getChapterIndex();
    }

    public Bookmark convertToDb() {
        Bookmark bookmark = new Bookmark();
        bookmark.setBookId(bookId);
        bookmark.setBookmarkId(bookmarkId);
        bookmark.setLastCharPosition(lastCharPosition);
        bookmark.setName(name);
        bookmark.setChapterIndex(chapterIndex);
        return bookmark;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getLastCharPosition() {
        return lastCharPosition;
    }

    public void setLastCharPosition(long lastCharPosition) {
        this.lastCharPosition = lastCharPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }
}
