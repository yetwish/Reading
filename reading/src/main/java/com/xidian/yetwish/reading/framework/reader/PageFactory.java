package com.xidian.yetwish.reading.framework.reader;


import android.content.Context;
import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.FutureCallback;
import com.xidian.yetwish.reading.BaseApplication;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedPage;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.thread.ThreadFactory;
import com.xidian.yetwish.reading.framework.thread.ThreadRunner;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Yetwish on 2016/4/24 0024.
 */
public class PageFactory {

    public static PageDivider createPageDivider(Context context){
        return new PageDivider(context);
    }
}
