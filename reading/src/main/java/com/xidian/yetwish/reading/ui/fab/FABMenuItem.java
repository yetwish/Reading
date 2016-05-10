package com.xidian.yetwish.reading.ui.fab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xidian.yetwish.reading.R;

/**
 * fab_sub menu, consist of a floating text and a floating icon
 * Created by Yetwish on 2016/4/13 0013.
 */
public class FABMenuItem extends LinearLayout {

    private TextView tvTitle;
    private ImageButton ibIcon;
    private Context mContext;

    public FABMenuItem(Context context) {
        super(context);
        this.mContext = context;
    }

    public FABMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        //true : attach the inflated view to the layout
        LayoutInflater.from(mContext).inflate(R.layout.fab_menu_item, this, true);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        String text = "";
        int resId = R.mipmap.ic_search_white_24dp;
        if (attrs != null) {
            TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.FABMenuItem);
            text = ta.getString(R.styleable.FABMenuItem_text);
            resId = ta.getResourceId(R.styleable.FABMenuItem_src, R.mipmap.ic_search_white_24dp);
            ta.recycle();
        }
        tvTitle = (TextView) findViewById(R.id.tvFABTitle);
        ibIcon = (ImageButton) findViewById(R.id.ibFABItemIcon);

        tvTitle.setText(text);
        ibIcon.setImageResource(resId);

    }

    public void setText(String text){
        tvTitle.setText(text);
    }

    public void setImageRes(int resId){
        ibIcon.setImageResource(resId);
    }

}
