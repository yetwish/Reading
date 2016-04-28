package com.xidian.yetwish.reading.framework.vo.reader;

import com.xidian.yetwish.reading.framework.database.generator.Page;

/**
 * 页面实体类，position 包含头不包含尾
 * Created by Yetwish on 2016/4/24 0024.
 */
public class PageVo {

    private String filePath;
    private String pageId;
    private String chapterId;
    private long firstCharPosition;
    private long lastCharPosition;
    private String content;

    public PageVo(Page page){

    }

    public Page asPage(){
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
}
