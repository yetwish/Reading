package com.xidian.yetwish.reading.framework.vo.reader;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.xidian.yetwish.reading.framework.database.generator.Chapter;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

/**
 * 章节entity
 * Created by Yetwish on 2016/4/24 0024.
 */
public class ChapterVo {

    private String filePath;
    private String chapterId;
    private String bookId;
    private String name;
    private long firstCharPosition;
    private long lastCharPosition;
    private int pageNumber;

    public ChapterVo(){

    }

    public ChapterVo(String filePath, String bookId, String name, long firstCharPosition, long lastCharPosition){
        this.chapterId = BookUtils.generateSequenceId();
        this.filePath = filePath;
        this.bookId = bookId;
        this.name = name;
        this.firstCharPosition = firstCharPosition;
        this.lastCharPosition = lastCharPosition;
    }

    public ChapterVo(Chapter chapter){

    }

    public Chapter convertToDb(){
        Chapter chapter = new Chapter();

        return chapter;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
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
                .append(" cid "+chapterId)
                .append(" bid "+bookId)
                .append(" name "+name)
                .append(" firstPosition "+ firstCharPosition)
                .append(" lastPosition "+lastCharPosition)
                .append(" ]");
        return sb.toString();
    }
}
