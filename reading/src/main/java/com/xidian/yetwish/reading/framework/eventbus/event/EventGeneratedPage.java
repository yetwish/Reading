package com.xidian.yetwish.reading.framework.eventbus.event;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;

/**
 * Created by Yetwish on 2016/5/3 0003.
 */
public class EventGeneratedPage {

    private ChapterVo mChapter;

    private ImmutableList<PageVo> mPageList;

    public EventGeneratedPage(ChapterVo chapter, ImmutableList<PageVo> pageList) {
        this.mChapter = chapter;
        this.mPageList = pageList;
    }

    public ChapterVo getChapter() {
        return mChapter;
    }

    public ImmutableList<PageVo> getPageList() {
        return mPageList;
    }

}
