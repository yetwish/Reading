package com.xidian.yetwish.reading.ui.widget.popuplistview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.common_adapter.OnItemLongClickListener;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;
import com.xidian.yetwish.reading.framework.vo.NoteVo;

import java.util.List;

/**
 * Created by Baobomb on 2015/9/25.
 */
public class PopupListView extends RelativeLayout {
    Context context;
    ListView listView;
    LinearLayout extendView;
    PopupListAdapter popupListAdapter;
    View extendPopupView;
    View extendInnerView;
    Handler handler = new Handler();
    int startY;
    int moveY = 0;
    //    int heightSpace = 0;
    int innerViewAlphaVal = 0;
    int listViewAlphaVal = 10;

    private LayoutParams mListViewParams;

    private int popupItem = -1;

    private OnItemLongClickListener<NoteVo> mLongClickListener;

    public void setItemLongClickListener(OnItemLongClickListener<NoteVo> listener){
        mLongClickListener = listener;
    }

    public PopupListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        popupListAdapter = new PopupListAdapter();
    }

    private PopupStatusChangeListener mPopupListener;

    public interface PopupStatusChangeListener {

        void onPopup(int position);

        void onCollapse();
    }

    public void setPopupItemClickListener(PopupStatusChangeListener listener) {
        this.mPopupListener = listener;
    }


    public void init(ListView customListView) {
//        setHeightSpace();
        mListViewParams = new LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int height = ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusHeight(context)
                - context.getResources().getDimensionPixelSize(R.dimen.actionbar_height);
        LayoutParams  extendViewParams= new LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, height);
        if (customListView == null) {
            listView = new ListView(context);
        } else {
            listView = customListView;
        }
        if (extendView == null) {
            extendView = new LinearLayout(context);
            extendView.setOrientation(LinearLayout.VERTICAL);
        }
        listView.setDivider(null);
        listView.setLayoutParams(mListViewParams);
        listView.setAdapter(popupListAdapter);
        listView.setOnItemClickListener(extend);
        listView.setOnItemLongClickListener(longClickListener);
        listView.setVerticalScrollBarEnabled(false);
        extendView.setLayoutParams(extendViewParams);
        extendView.setVisibility(GONE);
        this.addView(listView);
        this.addView(extendView);
    }

    public void setItemViews(List<? extends PopupView> items) {
        popupListAdapter.setItems(items);
    }

    public void refresh(){
        popupListAdapter.notifyDataSetChanged();
        if(isItemZoomIn()){
            extendView.removeAllViews();
            extendPopupView = ((PopupView) popupListAdapter.getItem(popupItem)).getPopupView();
            extendInnerView = ((PopupView) popupListAdapter.getItem(popupItem)).getExtendView();
            extendView.addView(extendPopupView);
            extendView.addView(extendInnerView);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int height = getResources().getDimensionPixelSize(R.dimen.item_note_height);
        final int margin = getResources().getDimensionPixelSize(R.dimen.item_note_margin);
        mListViewParams.height = (height + margin * 2) * popupListAdapter.getCount() + margin * 2;
        listView.setLayoutParams(mListViewParams);
    }

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(mLongClickListener!=null){
                mLongClickListener.onItemLongClick(view,null,position);
            }
            return true;
        }
    };

    private AdapterView.OnItemClickListener extend = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (mPopupListener != null)
                mPopupListener.onPopup(i);
            moveY = startY = (int) view.getY();
            zoomIn(i, startY);
            popupItem = i;
        }
    };

    public void zoomIn(int i, int startY) {
        listView.setVisibility(GONE);
        if (extendPopupView != null) {
            extendPopupView = null;
        }
        extendPopupView = ((PopupView) popupListAdapter.getItem(i)).getPopupView();
        extendInnerView = ((PopupView) popupListAdapter.getItem(i)).getExtendView();
        extendView.addView(extendPopupView);
        extendPopupView.setY(startY);
        extendInnerView.setVisibility(GONE);
        extendView.addView(extendInnerView);
        extendView.setVisibility(VISIBLE);
        handler.postDelayed(zoomInRunnable, 100);
    }

    public void zoomOut() {
        handler.removeCallbacks(zoomInRunnable);
        handler.postDelayed(zoomOutRunnable, 1);
        if (mPopupListener != null) {
            mPopupListener.onCollapse();
        }
        popupItem = -1;
    }

    public boolean isItemZoomIn() {
        if (extendView.getVisibility() == VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public Runnable zoomInRunnable = new Runnable() {
        @Override
        public void run() {
            if (listViewAlphaVal >= 0) {
                listView.setAlpha(listViewAlphaVal * 0.1f);
                listViewAlphaVal--;
                handler.postDelayed(zoomInRunnable, 10);
            } else {
                if (listView.getVisibility() != GONE) {
                    listView.setVisibility(GONE);
                }
                if (moveY > 0) {
                    moveY -= startY / 10;
                    extendPopupView.setY(moveY);
                    handler.postDelayed(zoomInRunnable, 10);
                } else {
                    extendPopupView.setY(0);
                    if (innerViewAlphaVal < 10) {
                        extendInnerView.setAlpha(innerViewAlphaVal * 0.1f);
                        extendInnerView.setVisibility(VISIBLE);
                        innerViewAlphaVal++;
                        handler.postDelayed(zoomInRunnable, 10);
                    }
                }
            }
        }
    };

    public Runnable zoomOutRunnable = new Runnable() {
        @Override
        public void run() {

            if (innerViewAlphaVal > 0) {
                extendInnerView.setAlpha(innerViewAlphaVal * 0.1f);
                innerViewAlphaVal--;
                handler.postDelayed(zoomOutRunnable, 1);
            } else {
                if (extendInnerView.getVisibility() != GONE) {
                    extendInnerView.setVisibility(GONE);
                }
                if (moveY < startY) {
                    moveY += (startY) / 10;
                    extendPopupView.setY(moveY);
                    handler.postDelayed(zoomOutRunnable, 10);
                } else {
                    if (listViewAlphaVal < 10) {
                        listViewAlphaVal++;
                        if (listView.getVisibility() == GONE) {
                            listView.setVisibility(VISIBLE);
                        }
                        listView.setAlpha(listViewAlphaVal * 0.1f);
                        handler.postDelayed(zoomOutRunnable, 10);
                    } else {
                        if (extendPopupView != null) {
                            extendPopupView.setY(startY);
                            extendView.setVisibility(GONE);
                            extendView.removeAllViews();
                            extendPopupView = null;
                        }
                    }
                }
            }


        }
    };

//    public void setHeightSpace() {
//        int actionBarHeight = 0;
//        TypedValue tv = new TypedValue();
//        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources()
//                    .getDisplayMetrics());
//        }
//
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        this.heightSpace = actionBarHeight + result;
//    }
}
