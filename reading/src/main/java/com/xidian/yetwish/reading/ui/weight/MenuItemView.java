package com.xidian.yetwish.reading.ui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.vo.MenuItem;

/**
 * view of menuItem
 * Created by Yetwish on 2016/4/11 0011.
 */
public class MenuItemView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private ImageView ivIcon;
    private TextView tvText;

    private OnItemClickListener mClickListener;

    private boolean arrowVisibility;

    /**
     * arrow on the right;the visibility will be gone in default
     */
    private ImageView ivArrow;


    //TODO 更优  view inflate??????

    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        int resId = ta.getResourceId(R.styleable.MenuItemView_itemIcon, R.mipmap.ic_launcher);
        String text = ta.getString(R.styleable.MenuItemView_itemText);
        arrowVisibility = ta.getBoolean(R.styleable.MenuItemView_hasArrow, false);
        ta.recycle();
        initView(resId, text);
    }

    public MenuItemView(Context context, MenuItem menuItem) {
        this(context);
        initView(menuItem.getIconResId(), menuItem.getText());
    }

    private void initView(int resId, String text) {
        LayoutInflater.from(mContext).inflate(R.layout.item_menu, this);
        ivIcon = (ImageView) findViewById(R.id.iv_item_icon);
        tvText = (TextView) findViewById(R.id.tv_item_text);
        ivArrow = (ImageView) findViewById(R.id.iv_item_arrow);
        ivIcon.setImageResource(resId);
        tvText.setText(text);
        setArrowVisible(arrowVisibility);

        this.setOnClickListener(this);
    }


    protected MenuItemView(Context context) {
        super(context);
        this.mContext = context;

    }

    /**
     * set the visibility of arrow. true: visible ; false: invisible
     *
     * @param visibility
     */
    public void setArrowVisible(boolean visibility) {
        arrowVisibility = visibility;
        if (arrowVisibility)
            ivArrow.setVisibility(VISIBLE);
        else
            ivArrow.setVisibility(GONE);
    }

    /**
     * set item click listener
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public void onClick(View v) {

        //TODO 点击状态改变背景颜色
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (ivArrow.isShown()) {

        }

        if (mClickListener != null) {
            mClickListener.onItemClick(this);
        }
    }


    /**
     * item click listener
     */
    public interface OnItemClickListener {

        /**
         * when item is clicked, invoke this method
         *
         * @param view
         */
        void onItemClick(View view);
    }


}
