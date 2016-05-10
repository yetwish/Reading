package com.xidian.yetwish.reading.framework.eventbus.event;

import com.xidian.yetwish.reading.framework.vo.BookVo;

/**
 * Created by Yetwish on 2016/5/11 0011.
 */
public class EventBookRead {

    private BookVo mBook;

    public EventBookRead(BookVo book) {
        this.mBook = book;
    }

    public BookVo getReadBook(){
        return mBook;
    }
}
