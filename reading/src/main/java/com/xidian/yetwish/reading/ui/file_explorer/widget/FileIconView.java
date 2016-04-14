package com.xidian.yetwish.reading.ui.file_explorer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.xidian.yetwish.reading.R;

/**
 * 根据text(String)获取iconView
 * Created by Yetwish on 2016/4/14 0014.
 */
public class FileIconView extends View{

    private static final int COLOR_DEFAULT_BG = R.color.colorSlideMenu;
    private static final int COLOR_DEFAULT_TEXT = R.color.colorText;

    private Context mContext;
    private Paint mPaint;
    private String mText;
    private int mBgColor;
    private int mTextColor;


    public FileIconView(Context context,String text){
        super(context);
        this.mContext = context;
        this.mText = text;
        initPaint();
    }

    public FileIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initPaint(){
        mPaint = new Paint();
    }


    public void setText(String text){
        this.mText = text;
        invalidate();
    }


    public void setBackground(int color){
        this.mBgColor = color;
        invalidate();
    }

    public void setTextColor(int color){
        this.mTextColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawBg
        drawBg(canvas);
        //drawText
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {

    }

    private void drawBg(Canvas canvas) {

    }
}
