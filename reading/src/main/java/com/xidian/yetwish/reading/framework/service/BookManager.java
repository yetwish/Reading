package com.xidian.yetwish.reading.framework.service;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.manager.DbBookManager;
import com.xidian.yetwish.reading.framework.reader.ChapterFactory;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;

/**
 * Created by Yetwish on 2016/5/8 0008.
 */
public class BookManager {


//    public ImmutableList<ChapterVo> getChapterListOfBook(BookVo book) {
//        ImmutableList<ChapterVo> chapterList = DatabaseManager.getsInstance().getChapterManager()
//                                                            .queryByBookId(book.getBookId());
//        if(chapterList.size() == 0){
////            ChapterFactory.getsInstance().concurrentReadFile(book);
//        }
//
//    }
//
//    public ImmutableList<PageVo> getPageListOfChapter() {
//
//    }
}
