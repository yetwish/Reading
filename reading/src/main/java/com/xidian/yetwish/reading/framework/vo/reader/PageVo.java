package com.xidian.yetwish.reading.framework.vo.reader;

import com.xidian.yetwish.reading.framework.database.generator.Page;
import com.xidian.yetwish.reading.framework.utils.BookUtils;

/**
 * 页面实体类，position 包含头不包含尾
 * Created by Yetwish on 2016/4/24 0024.
 */
public class PageVo {

    private long pageId;
    private long chapterId;
    private long bookId;
    private long firstCharPosition;
    private long lastCharPosition;
    private String content;

    public PageVo(long bookId, long chapterId, long firstCharPosition, long lastCharPosition, String content) {
        this.pageId = BookUtils.generateSequenceId();
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.firstCharPosition = firstCharPosition;
        this.lastCharPosition = lastCharPosition;
        this.content = content;
    }

    public PageVo(Page page) {
        this.pageId = page.getPageId();
        this.bookId = page.getBookId();
        this.chapterId = page.getChapterId();
        this.firstCharPosition = page.getFirstCharPosition();
        this.lastCharPosition = page.getLastCharPosition();
        this.content = page.getContent();
    }

    public Page convertToDb() {
        Page page = new Page();
        page.setPageId(pageId);
        page.setBookId(bookId);
        page.setChapterId(chapterId);
        page.setFirstCharPosition(firstCharPosition);
        page.setLastCharPosition(lastCharPosition);
        page.setContent(content);
        return page;
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
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
