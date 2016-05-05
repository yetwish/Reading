package com.xidian.yetwish.reading.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.google.common.base.Splitter;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.reader.ReaderController;
import com.xidian.yetwish.reading.framework.utils.SystemUtils;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;

/**
 * 文本阅读view
 * Created by Yetwish on 2016/4/23 0023.
 */
public class ReaderView extends View {

    public static final int THEME_DAY = 0;
    public static final int THEME_NIGHT = 0x01;

    public static final int MODE_SLIDE = 0;
    public static final int MODE_FLIP = 0x01;

    private Context mContext;

    private ReaderController mController;

    private Paint mContentPaint;
    private Paint mChapterPaint;
    private Paint mInfoPaint;
    private Paint mBgPaint;

    private int mContentColor;
    private int mBgColor;
    private int mInfoColor;
    private int mMenuBarColor;

    private int mInfoTextSize;
    private int mContentTextSize;

    private int mTheme;
    private int mFlagMode;

    private PageVo mPage;
    private Iterable<String> mContent;

    private String mChapterName;
    private float mProgress;

    public ReaderView(Context context) {
        this(context, null);

    }

    public ReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mController = ReaderController.getInstance(mContext);
        initFromAttrs(attrs);
        initPaint();
    }

    private void initFromAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.ReaderView);
            mTheme = ta.getInt(R.styleable.ReaderView_Theme, THEME_DAY);
            mFlagMode = ta.getInt(R.styleable.ReaderView_flipMode, MODE_SLIDE);
            mContentTextSize = ta.getDimensionPixelSize(R.styleable.ReaderView_textSize,
                    mController.getTextSize());
            ta.recycle();
        } else {
            mTheme = THEME_DAY;
            mFlagMode = MODE_SLIDE;
            mContentTextSize = mController.getTextSize();
        }
        setColorByTheme();
        mInfoTextSize = mContext.getResources().getDimensionPixelOffset(R.dimen.reader_info_size);
    }


    private void setColorByTheme() {
        if (mTheme == THEME_DAY) {
            mContentColor = ContextCompat.getColor(mContext, R.color.colorPrimaryText);
            mInfoColor = ContextCompat.getColor(mContext, R.color.colorSecondaryText);
            mMenuBarColor = ContextCompat.getColor(mContext, R.color.colorPrimaryLight);
            mBgColor = ContextCompat.getColor(mContext, R.color.colorTransparent); //白天模式无背景色
        } else {
            // mTheme == THEME_NIGHT
        }
    }

    private void initPaint() {
        mContentPaint = new Paint();
        mContentPaint.setTextSize(mContentTextSize);
        mContentPaint.setColor(mContentColor);

        mChapterPaint = new Paint();
        mChapterPaint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.reader_chapter_size));
        mChapterPaint.setColor(ContextCompat.getColor(mContext, R.color.colorChapterText));

        mInfoPaint = new Paint();
        mInfoPaint.setTextSize(mInfoTextSize);
        mInfoPaint.setColor(mInfoColor);
        mInfoPaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
    }

    public void setData(PageVo page) {
        this.mPage = page;
        mContent = Splitter.on("\n")
                .omitEmptyStrings()
                .split(mPage.getContent());
        //获取章节名称和进度
        mChapterName = "第一章 降临神界";
        mProgress = 0.1233f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPage == null)
            return;
        int textHeight = mController.getTextHeight();
        int col = mController.getColOfPage();
        int rows = mController.getRowOfPage();
        int marginTop = mController.getTopMargin();
        int marginLeft = mController.getHorizontalMargin();
        int offset = (mController.getWidth() - col * mController.getTextWidth() - marginLeft * 2) / 2;
        marginLeft += offset;
        int j = 0;
        for (String line : mContent) {
            for (int i = 0; j < rows && i < line.length(); i += col, j++) {
                canvas.drawText(line, i, i + col <= line.length() ? i + col : line.length(),
                        marginLeft, marginTop + j * textHeight, mContentPaint);
            }
        }

        //todo 时间监听?
        canvas.drawText(SystemUtils.getCurrentTimeText(), marginLeft, marginTop + rows * textHeight, mInfoPaint);

        int width = (int) Math.ceil(mInfoPaint.measureText(mProgress + "%"));
        //draw progress
        canvas.drawText(mProgress * 100 + "%", mController.getWidth() - marginLeft - width,
                marginTop + rows * textHeight, mInfoPaint);

        canvas.drawText(mChapterName, marginLeft, marginTop / 2 - textHeight / 4, mInfoPaint);
        //显示电量  监听

    }

}
