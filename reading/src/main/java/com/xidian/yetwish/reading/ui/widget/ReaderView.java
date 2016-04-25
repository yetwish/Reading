package com.xidian.yetwish.reading.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.vo.reader.PageEntity;
import com.xidian.yetwish.reading.framework.utils.Constant;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;

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

    private Paint mContentPaint;
    private Paint mInfoPaint;
    private Paint mBgPaint;

    private int mContentColor;
    private int mBgColor;
    private int mInfoColor;
    private int mMenuBarColor;

    private int mInfoTextSize;
    private int mContentTextSize;

    private int mMarginOutside;

    private int mWidth;
    private int mHeight;

    private int mLineMargin;
    private int mTextMargin;

    private int mTheme;
    private int mFlagMode;

    private PageEntity mPageEntity;

    public ReaderView(Context context) {
        this(context, null);

    }

    public ReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initFromAttrs(attrs);
        initPaint();
    }

    private void initFromAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.ReaderView);
            mTheme = ta.getInt(R.styleable.ReaderView_Theme, THEME_DAY);
            mFlagMode = ta.getInt(R.styleable.ReaderView_flipMode, MODE_SLIDE);
            mContentTextSize = ta.getDimensionPixelSize(R.styleable.ReaderView_textSize,
                    mContext.getResources().getDimensionPixelSize(R.dimen.reader_content_size));
            ta.recycle();
        } else {
            mTheme = THEME_DAY;
            mFlagMode = MODE_SLIDE;
            mContentTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.reader_content_size);
        }
        setColorByTheme();
        mMarginOutside = mContext.getResources().getDimensionPixelSize(R.dimen.reader_content_size);

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

        mInfoPaint = new Paint();
        mInfoPaint.setTextSize(mInfoTextSize);
        mInfoPaint.setColor(mInfoColor);
        mInfoPaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
    }

    public void setData(PageEntity pageEntity) {
        this.mPageEntity = pageEntity;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getWidth();
        mHeight = getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if(mPageEntity == null)
//            return;

        int lineCount = 21;
        int textHeight = 60;
        int rows = ScreenUtils.getScreenHeight(mContext) / textHeight;
        for (int i = 0, j = 0; j < rows && i < Constant.TXT_EXAMPLE.length(); i += lineCount, j++) {
            canvas.drawText(Constant.TXT_EXAMPLE, i, i + lineCount <= Constant.TXT_EXAMPLE.length() ? i + lineCount : Constant.TXT_EXAMPLE.length(), mMarginOutside, mMarginOutside + j * textHeight, mContentPaint);
        }
    }

}
