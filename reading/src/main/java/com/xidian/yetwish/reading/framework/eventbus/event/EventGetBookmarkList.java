package com.xidian.yetwish.reading.framework.eventbus.event;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.vo.reader.BookmarkVo;

/**
 * Created by Yetwish on 2016/5/12 0012.
 */
public class EventGetBookmarkList {
    private ImmutableList<BookmarkVo> mList;

    public EventGetBookmarkList(ImmutableList<BookmarkVo> list){
        this.mList = list;
    }

    public ImmutableList<BookmarkVo> getBookmarkList(){
        return mList;
    }
}
