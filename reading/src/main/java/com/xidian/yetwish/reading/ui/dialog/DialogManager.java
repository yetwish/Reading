package com.xidian.yetwish.reading.ui.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xidian.yetwish.reading.R;


/**
 * Created by Yetwish on 2016/4/20 0020.
 */
public class DialogManager {

    private MaterialDialog mDialog;

    private DialogManager() {
    }

    private static class DialogManagerHolder {
        private static final DialogManager instance = new DialogManager();
    }

    public static final DialogManager getInstance() {
        return DialogManagerHolder.instance;
    }

    public void showProgressDialog(Context context) {
        showProgressDialog(context, context.getString(R.string.loading));
    }

    public void showProgressDialog(Context context, String msg) {
        mDialog = new MaterialDialog.Builder(context)
                .content(msg)
                .progress(true, 0)
                .backgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
                .contentColor(ContextCompat.getColor(context,R.color.colorPrimary))
                .show();
    }


    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

    private View.OnClickListener mPositiveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener mNegativeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
