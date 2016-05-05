package com.xidian.yetwish.reading.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xidian.yetwish.reading.R;


/**
 * base activity,dialog and something
 * Created by Yetwish on 2016/4/8 0008.
 */
public class BaseActivity extends AppCompatActivity {

    private MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void showSoftKeyBoard(Activity avitvity, View view) {
        InputMethodManager imm = (InputMethodManager) avitvity.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showProgressDialog() {
        showProgressDialog(getString(R.string.loading));
    }

    protected void showProgressDialog(DialogInterface.OnDismissListener listener) {
        showProgressDialog(getString(R.string.loading), listener);
    }

    protected void showProgressDialog(String msg) {
        showProgressDialog(msg, false, null);
    }

    protected void showProgressDialog(String msg, DialogInterface.OnDismissListener listener) {
        showProgressDialog(msg, false, listener);
    }

    protected void showProgressDialog(String msg, boolean cancelable, DialogInterface.OnDismissListener listener) {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this)
                    .content(msg)
                    .progress(true, 0)
                    .backgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
                    .contentColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .canceledOnTouchOutside(cancelable)
                    .dismissListener(listener)
                    .show();
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyDialog();
    }

    private void hideDialog() {
        hideProgressDialog();
    }

    private void destroyDialog() {
        hideDialog();
        mProgressDialog = null;
    }
}
