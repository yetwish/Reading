package com.xidian.yetwish.reading.framework.eventbus.event;


import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.vo.BookVo;

import java.util.List;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class EventAddBooks {

    private ImmutableList<BookVo> bookVoList;

    public EventAddBooks(ImmutableList<BookVo> bookEntities) {
        this.bookVoList = bookEntities;
    }

    public List<BookVo> getBookList(){
        return bookVoList;
    }
}
