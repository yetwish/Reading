package com.xidian.yetwish.reading.ui.reader;

/**
 * Created by Yetwish on 2016/5/7 0007.
 */
public interface IReaderProgressChangeListener {

    void onProgressChanged(int chapterIndex, long position,boolean withPosition);
}
