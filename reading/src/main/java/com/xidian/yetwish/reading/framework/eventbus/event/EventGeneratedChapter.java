package com.xidian.yetwish.reading.framework.eventbus.event;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;

/**
 * 生成完章节的event
 * Created by Yetwish on 2016/5/3 0003.
 */
public class EventGeneratedChapter {

    private ImmutableList<ChapterVo> mList;

    public EventGeneratedChapter(ImmutableList<ChapterVo> chapters){
        mList = chapters;
    }

    public ImmutableList<ChapterVo> getChapterList(){
        return mList;
    }
}
