package com.xidian.yetwish.reading.framework.database.generator;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table BOOKMARK.
 */
public class Bookmark {

    private Long bookmarkId;
    private Long bookId;
    private Long lastCharPosition;
    private String name;
    private Integer chapterIndex;

    public Bookmark() {
    }

    public Bookmark(Long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public Bookmark(Long bookmarkId, Long bookId, Long lastCharPosition, String name, Integer chapterIndex) {
        this.bookmarkId = bookmarkId;
        this.bookId = bookId;
        this.lastCharPosition = lastCharPosition;
        this.name = name;
        this.chapterIndex = chapterIndex;
    }

    public Long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(Long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getLastCharPosition() {
        return lastCharPosition;
    }

    public void setLastCharPosition(Long lastCharPosition) {
        this.lastCharPosition = lastCharPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(Integer chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

}
