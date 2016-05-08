package com.xidian.yetwish.reading.framework.database.generator;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CHAPTER.
 */
public class Chapter {

    private Long chapterId;
    private Long bookId;
    private String path;
    private String name;
    private Long firstCharPosition;
    private Long lastCharPosition;
    private Integer pageNumber;

    public Chapter() {
    }

    public Chapter(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Chapter(Long chapterId, Long bookId, String path, String name, Long firstCharPosition, Long lastCharPosition, Integer pageNumber) {
        this.chapterId = chapterId;
        this.bookId = bookId;
        this.path = path;
        this.name = name;
        this.firstCharPosition = firstCharPosition;
        this.lastCharPosition = lastCharPosition;
        this.pageNumber = pageNumber;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFirstCharPosition() {
        return firstCharPosition;
    }

    public void setFirstCharPosition(Long firstCharPosition) {
        this.firstCharPosition = firstCharPosition;
    }

    public Long getLastCharPosition() {
        return lastCharPosition;
    }

    public void setLastCharPosition(Long lastCharPosition) {
        this.lastCharPosition = lastCharPosition;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

}
