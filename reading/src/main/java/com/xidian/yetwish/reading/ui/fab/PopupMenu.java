package com.xidian.yetwish.reading.ui.fab;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.SlideMenuActivity;
import com.xidian.yetwish.reading.utils.ScreenUtils;

/**
 * Popup window for FAB's menu  TODO refactoring + animation + frameBg
 * Created by Yetwish on 2016/4/13 0013.
 */
public class PopupMenu extends PopupWindow implements View.OnClickListener {

    private SlideMenuActivity mContext;

    private FABMenuItem menuSearch;
    private FABMenuItem menuFolder;

    private OnFABItemClickListener mFABItemClickListener;

    public PopupMenu(SlideMenuActivity context, OnFABItemClickListener listener) {
        this.mContext = context;
        this.mFABItemClickListener = listener;
        initMenu();
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

        menuSearch = (FABMenuItem) root.findViewById(R.id.fabItemSearch);
        menuFolder = (FABMenuItem) root.findViewById(R.id.fabItemFolder);

        menuSearch.setOnClickListener(this);
        menuFolder.setOnClickListener(this);
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
            case R.id.fabItemSearch:
                if (mFABItemClickListener != null)
                    mFABItemClickListener.onAutoSearching();
                hide();
                break;
            case R.id.fabItemFolder:
                if (mFABItemClickListener != null)
                    mFABItemClickListener.onChoseManually();
                hide();
                break;
        }
    }


}
