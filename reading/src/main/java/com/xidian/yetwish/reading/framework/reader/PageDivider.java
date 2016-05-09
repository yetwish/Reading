package com.xidian.yetwish.reading.framework.reader;

import android.content.Context;
import android.text.TextUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.FutureCallback;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGeneratedPage;
import com.xidian.yetwish.reading.framework.thread.ThreadFactory;
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
 * Created by Yetwish on 2016/5/9 0009.
 */
public class PageDivider {

    private int mRow;
    private int mCol;

    PageDivider(Context context) {
        mRow = ReaderController.getInstance(context).getRowOfPage();
        mCol = ReaderController.getInstance(context).getColOfPage();
    }

    /**
     * todo 最后一个字符,首字缩进的处理
     *
     * @param chapter
     */
    public void paging(final ChapterVo chapter) {

        ThreadFactory.createThread().start(new Callable<ImmutableList<PageVo>>() {
            @Override
            public ImmutableList<PageVo> call() throws Exception {
                return doPaging(chapter);

            }
        }, new FutureCallback<ImmutableList<PageVo>>() {
            @Override
            public void onSuccess(ImmutableList<PageVo> result) {
                //将分页数据存入数据库。
                DatabaseManager.getsInstance().getPageManager().refresh(result);

                //通过eventBus将数据发送出去
                EventBusWrapper.getDefault().post(new EventGeneratedPage(chapter, result));
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 分页
     *
     * @param chapter
     */
    private ImmutableList<PageVo> doPaging(ChapterVo chapter) throws IOException {

        List<PageVo> pageList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(chapter.getFilePath()), ChapterDivider.CHARSET_GBK));
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
            if (curRow < mRow) {
                sbContent.append(line).append("\n");
//                LogUtils.w(sbContent.toString());
            } else if (curRow == mRow) {
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
                while (curRow > mRow) {
                    //截断
                    curRow -= row;
                    if (isFirst) {//todo
                        divideIndex = (mRow - curRow) * mCol - 2 + 1;
                        isFirst = false;
                    } else
                        divideIndex = (mRow - curRow) * mCol + 1;
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
        chapter.setPageNumber(pageList.size());
        reader.close();

        return ImmutableList.copyOf(pageList);
    }

    private int getRowOfText(String text) {
        int row = text.length() / mCol;
        int col = text.length() % mCol;
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
