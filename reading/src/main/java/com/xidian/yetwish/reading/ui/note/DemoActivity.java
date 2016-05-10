package com.xidian.yetwish.reading.ui.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.com.baobomb.popuplistview.PopupListView;
import com.xidian.yetwish.reading.ui.com.baobomb.popuplistview.PopupView;

import java.util.ArrayList;
import java.util.List;


public class DemoActivity extends Activity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DemoActivity.class);
        context.startActivity(intent);
    }

    PopupListView popupListView;
    ArrayList<PopupView> popupViews;
    int actionBarHeight;
    int p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        List<String> strs = new ArrayList<>(10);
        popupViews = new ArrayList<>();
        popupListView = (PopupListView) findViewById(R.id.popupListView);
        for (int i = 0; i < 10; i++) {
            p = i;
            strs.add("Note Item " + i);
            PopupView<String> popupView = new PopupView<String>(this, R.layout.item_note_list, strs.get(i)) {
                @Override
                public void setItemView(View view, String data) {
                    TextView textView = (TextView) view.findViewById(R.id.title);
                    textView.setText(data);
                }

                @Override
                public View setExtendView(View view,String data) {
                    View extendView;
                    if (view == null) {
                        extendView = LayoutInflater.from(getApplicationContext()).inflate(R
                                .layout.note_view, null);
                        TextView innerText = (TextView) extendView.findViewById(R.id.innerText);
                        innerText.setText(data);
                    } else {
                        extendView = view;
                    }
                    return extendView;
                }
            };
            popupViews.add(popupView);
        }
        popupListView.init(null);
        popupListView.setItemViews(popupViews);
    }

    @Override
    public void onBackPressed() {
        if (popupListView.isItemZoomIn()) {
            popupListView.zoomOut();
        } else {
            super.onBackPressed();
        }
    }
}
