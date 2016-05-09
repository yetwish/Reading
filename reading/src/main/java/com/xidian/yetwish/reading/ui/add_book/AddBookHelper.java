package com.xidian.yetwish.reading.ui.add_book;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.xidian.yetwish.reading.framework.FWHelper;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventAddBooks;
import com.xidian.yetwish.reading.framework.eventbus.event.EventAddedBook;
import com.xidian.yetwish.reading.framework.reader.ChapterFactory;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yetwish on 2016/5/8 0008.
 */
public class AddBookHelper {

    public AddBookHelper() {
        EventBusWrapper.getDefault().register(this);
        mAddedBook = new ArrayList<>();
        mCount = 0;
    }

    private int mCount;

    private List<BookVo> mAddedBook;

    private Runnable mListener;

    public void addBook(ImmutableList<File> fileList, Runnable listener) {
        this.mListener = listener;
        for (File file : fileList) {
            BookVo book = new BookVo(file.getAbsolutePath(), file.getName());
            mAddedBook.add(book);
        }
        for (BookVo book : mAddedBook) {
            try {
                ChapterFactory.createDivider().scanBook(book);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe
    public void onBookAdded(EventAddedBook event) {
        LogUtils.w("onBookAdded!");
        mCount++;
        if (mCount == mAddedBook.size()) {
            EventBusWrapper.getDefault().post(new EventAddBooks(ImmutableList.copyOf(mAddedBook)));
            if (mListener != null) {
                FWHelper.getInstance().getMainHandler().post(mListener);
            }
        }
    }


}
