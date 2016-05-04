package com.xidian.yetwish.reading.framework.vo.reader;

import com.xidian.yetwish.reading.framework.database.generator.Page;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

/**
 * 页面实体类，position 包含头不包含尾
 * Created by Yetwish on 2016/4/24 0024.
 */
public class PageVo {

    private String filePath;
    private String pageId;
    private String chapterId;
    private String bookId;
    private long firstCharPosition;
    private long lastCharPosition;
    private String content;

    public PageVo(String bookId,String chapterId,String filePath,long firstCharPosition,long lastCharPosition,String content){
        this.pageId = BookUtils.generateSequenceId();
        this.filePath = filePath;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.firstCharPosition = firstCharPosition;
        this.lastCharPosition = lastCharPosition;
        this.content = content;
    }

    public PageVo(Page page) {

    }

    public Page convertToDb() {
        Page page = new Page();

        return page;

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Page [")
//                .append(" cid ").append(chapterId)
//                .append(" bid ").append(bookId)
//                .append(" pid ").append(pageId)
//                .append(" firstPosition ").append(firstCharPosition)
//                .append(" lastPosition ").append(lastCharPosition)
                .append(" content ").append(content)
                .append(" ]");
        return sb.toString();
    }
}
