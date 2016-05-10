package com.xidian.yetwish.reading.ui.fab;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.SlideMenuActivity;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;

/**
 * Popup window for FAB's menu  TODO refactoring + animation + frameBg todo use fab in support design library
 * Created by Yetwish on 2016/4/13 0013.
 */
public class PopupMenu extends PopupWindow implements View.OnClickListener {

    private SlideMenuActivity mContext;


    public static final int ITEM_TOP = 0x01;
    public static final int ITEM_BOTTOM = 0x02;

    private FABMenuItem menuTop;
    private FABMenuItem menuBottom;

    private OnFABItemClickListener mFABItemClickListener;

    public PopupMenu(SlideMenuActivity context, OnFABItemClickListener listener) {
        this.mContext = context;
        this.mFABItemClickListener = listener;
        initMenu();
    }

    public void setTopItem(int resId, String text) {
        if (menuTop != null) {
            menuTop.setText(text);
            menuTop.setImageRes(resId);
        }
    }

    public void setBottomItem(int resId, String text) {
        if (menuBottom != null) {
            menuBottom.setText(text);
            menuBottom.setImageRes(resId);
        }
    }

    private void initMenu() {
        View root = LayoutInflater.from(mContext).inflate(R.layout.popup_menu, null);
        setContentView(root);
        this.setWidth(ScreenUtils.getScreenWidth(mContext));
        this.setHeight(ScreenUtils.getScreenHeight(mContext) -
                mContext.getSupportActionBar().getHeight() - ScreenUtils.getStatusHeight(mContext));
        //TODO bug touch outside should be interrupted
        this.setOutsideTouchable(true);
        this.update();
        root.findViewById(R.id.rlPopupBg).setOnClickListener(this);

        menuTop = (FABMenuItem) root.findViewById(R.id.fabItemTop);
        menuBottom = (FABMenuItem) root.findViewById(R.id.fabItemBottom);

        menuTop.setOnClickListener(this);
        menuBottom.setOnClickListener(this);
    }

    /**
     * 传入一个parentView，根据该view确定popupMenu的相对位置
     *
     * @param parentView
     */
    public void show(View parentView) {
        showAsDropDown(parentView, 0, 0);
    }

    public void hide() {
        //TODO add animation  ?? setOnDismissListener
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlPopupBg:
                hide();
                break;
            case R.id.fabItemTop:
                if (mFABItemClickListener != null)
                    mFABItemClickListener.onFabItemClick(ITEM_TOP);
                hide();
                break;
            case R.id.fabItemBottom:
                if (mFABItemClickListener != null)
                    mFABItemClickListener.onFabItemClick(ITEM_BOTTOM);
                hide();
                break;
        }
    }


}
