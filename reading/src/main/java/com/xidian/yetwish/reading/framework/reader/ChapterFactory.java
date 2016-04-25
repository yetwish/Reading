package com.xidian.yetwish.reading.framework.reader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.FutureCallback;
import com.xidian.yetwish.reading.framework.thread.ThreadRunner;
import com.xidian.yetwish.reading.framework.utils.AppUtils;
import com.xidian.yetwish.reading.framework.utils.LogUtils;

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

    public static ChapterFactory instance;

    public static final ChapterFactory getInstance() {
        if (instance == null) {
            synchronized (ChapterFactory.class) {
                if (instance == null) {
                    instance = new ChapterFactory();
                }

            }
        }
        return instance;
    }

    private static final String CHAPTER_FLAG_1 = "第.*回";
    private static final String CHAPTER_FLAG_2 = "第.*章";
    private static final String CHAPTER_MATCHER = "第";

    private static final String CHARSET_GBK = "GBK";

    private String mCharsetName = CHARSET_GBK;

    private Pattern mChapterPattern;
    private Pattern mChapterPattern2;


    private long mBookLen;
    private String mCurrentFilePath;

    /**
     * int : the position(index of char) of String in the file.
     */
    private Map<Integer, String> mChapterMap;

    private List<String> mChapterList;

    private BufferedReader mBufferedReader;

    private ChapterFactory() {
        mChapterPattern = Pattern.compile(CHAPTER_FLAG_1);
        mChapterPattern2 = Pattern.compile(CHAPTER_FLAG_2);
    }

    public void concurrentReadFile(String filePath) throws IOException {
        mCurrentFilePath = filePath;
        File bookFile = new File(mCurrentFilePath);
        mBookLen = bookFile.length();
        mChapterMap = new TreeMap<>();
        mChapterList = new ArrayList<>();
        List<Callable<String>> calls = new ArrayList<>();

        long eachThreadScanLength = 0;
        if (mCharsetName.equals(CHARSET_GBK)) {
            //每个线程(至少)遍历的char个数
            eachThreadScanLength = mBookLen / 2 / AppUtils.getNumCores();
        }
        LogUtils.w("start!");
        for (int i = 0; i < AppUtils.getNumCores(); i++) {
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
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(mCurrentFilePath), mCharsetName));
        CharStreams.skipFully(bufferedReader, from);
        String line;
        for (int i = (int) from; i < to; ) {
            line = bufferedReader.readLine();
            if (line == null) break;
//            LogUtils.w(line + Thread.currentThread().getName());
            if (line.startsWith(CHAPTER_MATCHER)) {
                synchronized (ChapterFactory.getInstance()) {
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
        for (Integer index : mChapterMap.keySet()) {
            chapterName = mChapterMap.get(index);
            m1 = mChapterPattern.matcher(chapterName);
            m2 = mChapterPattern2.matcher(chapterName);
            if (m1.find() || m2.find()) {
                mChapterList.add(chapterName);
                //add entities
            } else {
                LogUtils.w(chapterName);
//                mChapterMap.remove(index);//todo modify exception
            }
        }

        if(mChapterGeneratorListener != null){
            mChapterGeneratorListener.onChapterGenerated(getChapterList());
        }

    }

    public ImmutableList<String> getChapterList() {
        return ImmutableList.copyOf(mChapterList);
    }

    private OnChapterGeneratorListener mChapterGeneratorListener;

    public void setChapterGeneratorListener(OnChapterGeneratorListener listener){
        this.mChapterGeneratorListener = listener;
    }

    public interface OnChapterGeneratorListener{

        void onChapterGenerated(ImmutableList<String> chapterList);
    }

}
