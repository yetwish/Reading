package com.xidian.yetwish.reading.framework.eventbus.event;


import com.xidian.yetwish.reading.database.bean.Book;

import java.util.List;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class EventAddBooks {

    private List<Book> bookList;

    public EventAddBooks(List<Book> books) {
        this.bookList = books;
    }

    public List<Book> getBookList(){
        return bookList;
    }
}
