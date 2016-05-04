package com.xidian.yetwish.reading.framework.reader;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.FutureCallback;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedChapter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * chapter factory , to divide the book into chapters and save chapterEntities
 * Created by Yetwish on 2016/4/24 0024.
 */
public class ChapterFactory {

    private static ChapterFactory sInstance;

    public static ChapterFactory getsInstance() {
        if (sInstance == null) {
            synchronized (ChapterFactory.class) {
                if (sInstance == null) {
                    sInstance = new ChapterFactory();
                }

            }
        }
        return sInstance;
    }

    private static final String CHAPTER_FLAG_1 = "第.*回";
    private static final String CHAPTER_FLAG_2 = "第.*章";
    private static final String CHAPTER_MATCHER = "第";

    public static final String CHARSET_GBK = "GBK";
    public static final String CHARSET_UTF_8 = "UTF-8";

    private String mCharsetName = CHARSET_GBK;

    private Pattern mChapterPattern;
    private Pattern mChapterPattern2;


    private long mBookLen;
    private BookVo mCurrentBook;

    private List<ChapterVo> mChapterList;

    /**
     * int : the position(index of char) of String in the file.
     */
    private Map<Integer, String> mChapterMap;

    private ChapterFactory() {
        mChapterPattern = Pattern.compile(CHAPTER_FLAG_1);
        mChapterPattern2 = Pattern.compile(CHAPTER_FLAG_2);
        mChapterList = new ArrayList<>();
    }

    public void concurrentReadFile(BookVo book) throws IOException {
        //todo 判断该book是否生成过章节目录，如果生成过，则从数据库中获取
        mCurrentBook = book;
        File bookFile = new File(book.getFilePath());
        mBookLen = bookFile.length();
        mChapterMap = new TreeMap<>();
        List<Callable<String>> calls = new ArrayList<>();

        long eachThreadScanLength = 0;
        if (mCharsetName.equals(CHARSET_GBK)) {
            //每个线程(至少)遍历的char个数
            eachThreadScanLength = mBookLen / 2 / SystemUtils.getNumCores();
        }
        LogUtils.w("start!");
        for (int i = 0; i < SystemUtils.getNumCores(); i++) {
            final long from = i * eachThreadScanLength;
            final long to = (i + 1) * eachThreadScanLength;
            Callable<String> call = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    scanFileForChapters(from, to);
                    return null;
                }
            };
            calls.add(call);
        }
        ThreadRunner.getInstance().start(calls, new FutureCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                LogUtils.w("success!");
                generateChapterList();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.w("failure!");
            }
        });
    }

    /**
     * @param from
     * @throws IOException
     */
    private void scanFileForChapters(long from, long to) throws IOException {
        if (mCurrentBook == null) return;
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(mCurrentBook.getFilePath()), mCharsetName));
        CharStreams.skipFully(bufferedReader, from);
        String line;
        for (int i = (int) from; i < to; ) {
            line = bufferedReader.readLine();
            if (line == null) break;
//            LogUtils.w(line + Thread.currentThread().getName());
            if (line.startsWith(CHAPTER_MATCHER)) {
                synchronized (ChapterFactory.getsInstance()) {
                    mChapterMap.put(i, line);
                }
            }
            i += line.length();
        }
        bufferedReader.close();
    }


    public void generateChapterList() {
        if (mChapterMap == null || mChapterMap.size() == 0) return;
        Matcher m1, m2;
        String chapterName;
        mChapterList.clear();
        //全书总字符数
        int charNum = 0;
        boolean isFirst = true;
        for (Integer index : mChapterMap.keySet()) {
            chapterName = mChapterMap.get(index);
            m1 = mChapterPattern.matcher(chapterName);
            m2 = mChapterPattern2.matcher(chapterName);
            if (m1.find() || m2.find()) {
                //add entities
                ChapterVo entity = null;
                if (isFirst) {
                    entity = new ChapterVo(mCurrentBook.getFilePath(),
                            mCurrentBook.getBookId(), mChapterMap.get(index), 0, 0);
                    isFirst = false;
                } else {
                    entity = new ChapterVo(mCurrentBook.getFilePath(),
                            mCurrentBook.getBookId(), mChapterMap.get(index), index, 0);
                }
                if (mChapterList.size() > 0) {
                    mChapterList.get(mChapterList.size() - 1).setLastCharPosition(index);
                }
                mChapterList.add(entity);
            } else {
                LogUtils.w(chapterName);
//                mChapterMap.remove(index);//todo modify exception
            }
            charNum = index;
        }
        charNum += mChapterMap.get(charNum).length();
        mCurrentBook.setCharNumber(charNum);
        //生成章节列表
        mChapterList.get(mChapterList.size() - 1).setLastCharPosition(charNum);
        //将章节列表存入数据库
//        DatabaseManager.getsInstance().getChapterManager().refresh(mChapterList);

        //使用eventBus 实现数据传递
        EventBusWrapper.getDefault().post(new EventGeneratedChapter(ImmutableList.copyOf(mChapterList)));
    }

}
