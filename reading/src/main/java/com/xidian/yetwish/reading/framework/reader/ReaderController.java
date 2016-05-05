package com.xidian.yetwish.reading.framework.reader;

import android.content.Context;
import android.graphics.Paint;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;

/**
 * readerView控制器
 * Created by Yetwish on 2016/5/5 0005.
 */
public class ReaderController {

    private static ReaderController sInstance;

    public static ReaderController getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ReaderController.class) {
                if (sInstance == null) {
                    sInstance = new ReaderController(context);
                }
            }
        }
        return sInstance;
    }

    private Context mAppContext;

    private TextSize textSize;

    private int mTextSize;

    private int mWidth;

    private int mHeight;

    private int mLineMargin;

    private int mMarginHorizontal;

    private int mMarginTop;

    private int mRowOfPage;

    private int mColOfPage;

    private int mTextWidth;

    private int mTextHeight;

    private static final String TEXT_EXAM = "永";


    private ReaderController(Context context) {
        this.mAppContext = context.getApplicationContext();
        mWidth = ScreenUtils.getScreenWidth(mAppContext);
        mHeight = ScreenUtils.getScreenHeight(mAppContext);
        mLineMargin = mAppContext.getResources().getDimensionPixelOffset(R.dimen.reader_line_margin);
        mMarginHorizontal = mAppContext.getResources().getDimensionPixelOffset(R.dimen.reader_margin_left);
        mMarginTop = mAppContext.getResources().getDimensionPixelOffset(R.dimen.reader_margin_top);
        updateTextSize(TextSize.NORMAL);
    }

    private void updateFontInfo() {
        Paint paint = new Paint();
        paint.setTextSize(mTextSize);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float width[] = new float[TEXT_EXAM.length()];
        paint.getTextWidths(TEXT_EXAM, width);
        mTextWidth = (int) Math.ceil(width[0]);
        mTextHeight = (int) (fontMetrics.bottom - fontMetrics.top + mLineMargin);
    }

    public void updateTextSize(TextSize size) {
        if (textSize == size) return;
        textSize = size;
        mTextSize = mAppContext.getResources().getDimensionPixelOffset(textSize.getValue());
        updateFontInfo();
        updatePageInfo();
        //通知textSize已改变
    }

    private void updatePageInfo() {
        //行数： (屏幕高度-上下边距) /(字体高度+行距)
        mRowOfPage = (mHeight - mMarginTop) / mTextHeight;
        //列数：
        mColOfPage = (mWidth - mMarginHorizontal * 2) / mTextWidth;

        LogUtils.w("height" + mHeight + ", width " + mWidth);


        LogUtils.w("row " + mRowOfPage + "," + "col " + mColOfPage);
    }

    public int getTextHeight() {
        return mTextHeight;
    }

    public int getTextWidth(){
        return mTextWidth;
    }

    public int getRowOfPage() {
        return mRowOfPage;
    }

    public int getColOfPage() {
        return mColOfPage;
    }

    public int getTextSize() {
        return mTextSize;
    }



    public int getLineMargin() {
        return mLineMargin;
    }

    public int getHorizontalMargin() {
        return mMarginHorizontal;
    }

    public int getTopMargin() {
        return mMarginTop;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    /**
     * 提供三种字体供用户切换。
     */
    public enum TextSize {

        SMALL(R.dimen.reader_content_size_small),

        NORMAL(R.dimen.reader_content_size_normal),

        BIG(R.dimen.reader_content_size_big);

        private int value;

        TextSize(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

}
