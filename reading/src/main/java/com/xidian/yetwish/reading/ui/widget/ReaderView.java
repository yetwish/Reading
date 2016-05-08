package com.xidian.yetwish.reading.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.google.common.base.Splitter;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.reader.ReaderController;
import com.xidian.yetwish.reading.framework.utils.BookUtils;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.SystemUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;

/**
 * 文本阅读view  改进 不用viewpager
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
    private Paint mBoarderPaint;

    private int mContentColor;
    private int mBgColor;
    private int mInfoColor;
    private int mBoarderColor;

    private int mInfoTextSize;
    private int mContentTextSize;

    private int mTheme;
    private int mFlagMode;

    private PageVo mPage;
    private Iterable<String> mContent;

    private int mInfoTextHeight;

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
        mBoarderColor = ContextCompat.getColor(mContext, R.color.colorBlack);
    }


    private void setColorByTheme() {
        if (mTheme == THEME_DAY) {
            mContentColor = ContextCompat.getColor(mContext, R.color.colorPrimaryText);
            mInfoColor = ContextCompat.getColor(mContext, R.color.colorSecondaryText);
            mBgColor = ContextCompat.getColor(mContext, R.color.colorReaderBg); //白天模式无背景色
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
        Paint.FontMetrics fontMetrics = mInfoPaint.getFontMetrics();
        mInfoTextHeight = (int) Math.ceil(fontMetrics.bottom - fontMetrics.top);

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);


        mBoarderPaint = new Paint();
        mBoarderPaint.setColor(mBoarderColor);
        mBoarderPaint.setStrokeWidth(2);
    }

    public void setData(PageVo page) {
        this.mPage = page;
        mContent = Splitter.on("\n")
                .omitEmptyStrings()
                .split(mPage.getContent());
        getPageInfo();
    }

    private String mChapterName;
    private float mProgress;

    private static BookVo sBook;

    private static long sBookId;

    //获取章节名称和进度
    private void getPageInfo() {
        ChapterVo chapter = DatabaseManager.getsInstance().getChapterManager().query(mPage.getChapterId());
        if (chapter != null)
            mChapterName = chapter.getName();
        if (sBook == null || sBookId != mPage.getBookId()) {
            sBookId = mPage.getBookId();
            sBook = DatabaseManager.getsInstance().getBookManager().query(sBookId);
        }
        if (sBook != null)
            mProgress = mPage.getFirstCharPosition() * 100.0f / sBook.getCharNumber();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPage == null)
            return;
        int lineHeight = mController.getLineHeight();
        int col = mController.getColOfPage();
        int rows = mController.getRowOfPage();
        int marginTop = mController.getTopMargin();
        int marginLeft = mController.getHorizontalMargin();
        int offset = (mController.getWidth() - col * mController.getTextWidth() - marginLeft * 2) / 2;
        marginLeft += offset;

        int screenWidth = mController.getWidth();
        int screenHeight = mController.getHeight();
        //画背景
        canvas.drawRect(0, 0, screenWidth, screenHeight, mBgPaint);
        //画边框
        canvas.drawLine(screenWidth, 0, screenWidth, screenHeight, mBoarderPaint);

        int j = 0;
        for (String line : mContent) {
            for (int i = 0; j < rows && i < line.length(); i += col, j++) {
                canvas.drawText(line, i, i + col <= line.length() ? i + col : line.length(),
                        marginLeft, marginTop + j * lineHeight, mContentPaint);
            }
        }

        //todo 时间监听?
        canvas.drawText(SystemUtils.getCurrentTimeText(), marginLeft,
                marginTop + rows * lineHeight - mController.getLineMargin(), mInfoPaint);

        String progress = BookUtils.formatDigit(mProgress) + "%";

        int width = (int) Math.floor(mInfoPaint.measureText(progress));
        //draw progress
        canvas.drawText(progress, marginLeft + mController.getTextWidth() * col - width,
                marginTop + rows * lineHeight - mController.getLineMargin(), mInfoPaint);

        if (mChapterName != null)
            canvas.drawText(mChapterName, marginLeft, marginTop - mController.getLineMargin() - mInfoTextHeight, mInfoPaint);
        //显示电量  监听


    }

}
