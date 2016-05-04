package com.xidian.yetwish.reading.framework.reader;


import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.FutureCallback;
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
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Yetwish on 2016/4/24 0024.
 */
public class PageFactory {

    public static int DEFAULT_NUMBER = 100;

    private static int DEFAULT_ROWS = 19;

    private static int DEFAULT_COLS = 20;

    private static PageFactory sInstance;

    public static PageFactory getInstance() {
        if (sInstance == null) {
            synchronized (PageFactory.class) {
                if (sInstance == null) {
                    sInstance = new PageFactory();
                }
            }
        }
        return sInstance;
    }

    private PageFactory() {
    }

    /**
     * 每页中最多容纳的字符
     */
    private int eachPageCharNumber = DEFAULT_NUMBER;

    public int getEachPageCharNumber() {
        return eachPageCharNumber;
    }

    public void setEachPageCharNumber(int charNumber) {
        eachPageCharNumber = charNumber;
    }

    public void startPaging(BookVo book) {
        if (book == null) return;
        DatabaseManager.getsInstance().getChapterManager().query(book.getBookId(), new ApiCallback<ImmutableList<ChapterVo>>() {
            @Override
            public void onDataReceived(ImmutableList<ChapterVo> data) {
                startPaging(data);
            }

            @Override
            public void onException(int code, String reason) {
                LogUtils.w(reason);
            }
        });
    }

    public void startPaging(ImmutableList<ChapterVo> chapterList) {
        if (chapterList == null || chapterList.size() == 0)
            return;
        List<Callable<PageVo>> calls = new ArrayList<>();
        for (final ChapterVo chapter : chapterList) {
            Callable<PageVo> call = new Callable<PageVo>() {
                @Override
                public PageVo call() throws Exception {
                    doPaging(chapter);
                    return null;
                }
            };
            calls.add(call);


        }
        ThreadRunner.getInstance().start(calls, new FutureCallback<List<PageVo>>() {
            @Override
            public void onSuccess(List<PageVo> result) {
                //分页完成
                LogUtils.w("success!");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.w("something wrong happened!");
            }
        });
    }


    public void paging(final ChapterVo chapter) {

        if (isPaged(chapter)) {
            //从数据库中获取数据

            return;
        }
        ThreadFactory.createThread().start(new Callable<ImmutableList<PageVo>>() {
            @Override
            public ImmutableList<PageVo> call() throws Exception {
                return doPaging(chapter);

            }
        }, new FutureCallback<ImmutableList<PageVo>>() {
            @Override
            public void onSuccess(ImmutableList<PageVo> result) {
                //通过eventBus将数据发送出去
                EventBusWrapper.getDefault().post(new EventGeneratedPage(chapter,result));
                //将分页数据存入数据库。
//                DatabaseManager.getsInstance().getPageManager().refresh(result);
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 判断某一章节是否已经分过页
     *
     * @param chapter
     * @return
     */
    private boolean isPaged(ChapterVo chapter) {
        //查询数据库，判断是否已经分过页
        return false;
    }

    /**
     * 分页
     *
     * @param chapter
     */
    private synchronized ImmutableList<PageVo> doPaging(ChapterVo chapter) throws IOException {

        List<PageVo> pageList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(chapter.getFilePath()), ChapterFactory.CHARSET_GBK));
        String line;
        StringBuilder sbContent = new StringBuilder();
        int position = (int) chapter.getFirstCharPosition();
        if (position > 0)
            skip(reader, position - 1);
        int curRow = 0;
        int firstIndex = position;
        int divideIndex = 0;
//        LogUtils.w(chapter.getFirstCharPosition() + "," + chapter.getLastCharPosition());
        PageVo page;
        while ((line = reader.readLine()) != null && position < chapter.getLastCharPosition()) {
            if (TextUtils.isEmpty(line.trim())) {
                continue;
            }
            int row = getRowOfText(line);
            curRow += row;
            if (curRow < DEFAULT_ROWS) {
                sbContent.append(line).append("\n");
//                LogUtils.w(sbContent.toString());
            } else if (curRow == DEFAULT_ROWS) {
                sbContent.append(line).append("\n");
//                LogUtils.w("page " + sbContent.toString());
                //生成pageVo
                page = new PageVo(chapter.getBookId(), chapter.getChapterId(), chapter.getFilePath(),
                        firstIndex, position + line.length(), sbContent.toString());
                pageList.add(page);
                clearStringBuilder(sbContent);
                curRow = 0;
                firstIndex = position + line.length();
            } else {
                boolean isFirst = true;
                while (curRow > DEFAULT_ROWS) {
                    //截断
                    curRow -= row;
                    if (isFirst) {
                        divideIndex = (DEFAULT_ROWS - curRow) * DEFAULT_COLS - 2 + 1;
                        isFirst = false;
                    } else
                        divideIndex = (DEFAULT_ROWS - curRow) * DEFAULT_COLS + 1;
                    sbContent.append(line, 0, divideIndex);
                    //生成pageVo
                    page = new PageVo(chapter.getBookId(), chapter.getChapterId(), chapter.getFilePath(),
                            firstIndex, position + divideIndex, sbContent.toString());
                    pageList.add(page);
//                    LogUtils.w("page " + sbContent.toString());
                    clearStringBuilder(sbContent);
                    firstIndex = position + divideIndex;
                    sbContent.append(line, divideIndex, line.length());
                    curRow = 0;
                    row = getRowOfText(sbContent.toString());
                    clearStringBuilder(sbContent);
                    curRow += row;
                }//curRow < default_rows
                sbContent.append(line, divideIndex, line.length()).append("\n");
            }
            position += line.length();
        }
        //最后一页
        if (sbContent.length() != 0) {
            page = new PageVo(chapter.getBookId(), chapter.getChapterId(), chapter.getFilePath(),
                    firstIndex, position, sbContent.toString());
            pageList.add(page);
//            LogUtils.w(sbContent.toString());
            clearStringBuilder(sbContent);
        }
        reader.close();

        return ImmutableList.copyOf(pageList);
    }

    private int getRowOfText(String text) {
        int row = text.length() / DEFAULT_COLS;
        int col = text.length() % DEFAULT_COLS;
        if (col != 0)
            row = row + 1;
        return row;
    }


    private void clearStringBuilder(StringBuilder sb) {
        sb.delete(0, sb.length());
    }


    private void skip(BufferedReader reader, long charNumber) throws IOException {
        String line;
        long index = 0;
        while (index < charNumber && (line = reader.readLine()) != null) {
            index += line.length();
        }
    }

}
