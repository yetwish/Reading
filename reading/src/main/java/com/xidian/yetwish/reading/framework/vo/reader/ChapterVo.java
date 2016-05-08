package com.xidian.yetwish.reading.framework.vo.reader;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.xidian.yetwish.reading.framework.database.generator.Chapter;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

import java.io.File;
import java.util.Comparator;

/**
 * 章节entity
 * Created by Yetwish on 2016/4/24 0024.
 */
public class ChapterVo {

    private String filePath;
    private long chapterId;
    private long bookId;
    private String name;
    private long firstCharPosition;
    private long lastCharPosition;
    private int pageNumber;

    public ChapterVo(String filePath, long bookId, String name, long firstCharPosition, long lastCharPosition) {
        this.chapterId = BookUtils.generateSequenceId();
        this.filePath = filePath;
        this.bookId = bookId;
        this.name = name;
        this.firstCharPosition = firstCharPosition;
        this.lastCharPosition = lastCharPosition;
    }

    public ChapterVo(Chapter chapter) {
        this.chapterId = chapter.getChapterId();
        this.filePath = chapter.getPath();
        this.bookId = chapter.getBookId();
        this.name = chapter.getName();
        this.firstCharPosition = chapter.getFirstCharPosition();
        this.lastCharPosition = chapter.getLastCharPosition();
        this.pageNumber = chapter.getPageNumber();
    }

    public Chapter convertToDb() {
        Chapter chapter = new Chapter();
        chapter.setBookId(bookId);
        chapter.setPath(filePath);
        chapter.setChapterId(chapterId);
        chapter.setName(name);
        chapter.setFirstCharPosition(firstCharPosition);
        chapter.setLastCharPosition(lastCharPosition);
        chapter.setPageNumber(pageNumber);
        return chapter;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
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

    public long getFirstCharPosition() {
        return firstCharPosition;
    }

    public void setFirstCharPosition(long firstCharPosition) {
        this.firstCharPosition = firstCharPosition;
    }

    public long getLastCharPosition() {
        return lastCharPosition;
    }

    public void setLastCharPosition(long lastCharPosition) {
        this.lastCharPosition = lastCharPosition;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Chapter [")
                .append(" cid " + chapterId)
                .append(" bid " + bookId)
                .append(" name " + name)
                .append(" firstPosition " + firstCharPosition)
                .append(" lastPosition " + lastCharPosition)
                .append(" ]");
        return sb.toString();
    }
}
