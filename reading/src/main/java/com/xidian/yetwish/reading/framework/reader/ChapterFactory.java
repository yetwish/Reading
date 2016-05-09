package com.xidian.yetwish.reading.framework.reader;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.FutureCallback;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventAddedBook;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedChapter;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.thread.ThreadFactory;
import com.xidian.yetwish.reading.framework.thread.ThreadRunner;
import com.xidian.yetwish.reading.framework.utils.SystemUtils;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * todo 未采用多线程划分
 * chapter factory , to divide the book into chapters and save chapterEntities
 * Created by Yetwish on 2016/4/24 0024.
 */
public class ChapterFactory {

   public static ChapterDivider createDivider(){
       return new ChapterDivider();
   }

}
