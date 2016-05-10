package com.xidian.yetwish.reading.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.utils.LogUtils;


/**
 * base activity,dialog and something
 * Created by Yetwish on 2016/4/8 0008.
 */
public class BaseActivity extends AppCompatActivity {

    private MaterialDialog mProgressDialog;

    private MaterialDialog mInputDialog;

    protected Snackbar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void showSoftKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
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

    protected void showProgressDialog(CharSequence msg) {
        showProgressDialog(msg, false, null);
    }

    protected void showProgressDialog(CharSequence msg, DialogInterface.OnDismissListener listener) {
        showProgressDialog(msg, false, listener);
    }

    protected void showProgressDialog(CharSequence msg, boolean cancelable, DialogInterface.OnDismissListener listener) {
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

    protected void showInputDialog(CharSequence title, CharSequence hintText, MaterialDialog.InputCallback callback) {
        showInputDialog(title, hintText, "", callback);
    }

    protected void showInputDialog(CharSequence title, CharSequence hintText,
                                   CharSequence preFill, MaterialDialog.InputCallback callback) {
        if (mInputDialog == null) {
            mInputDialog = new MaterialDialog.Builder(this)
                    .title(title)
                    .backgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
                    .titleColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .contentColor(ContextCompat.getColor(this, R.color.colorText))
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(hintText, preFill, callback)
                    .show();

        } else if (!mInputDialog.isShowing()) {
            mInputDialog.getInputEditText().setText(preFill);
            mInputDialog.show();
        }
    }

    protected void showLongSnackBar(@NonNull View parentView, CharSequence msg) {
        showSnackBar(parentView, msg, Snackbar.LENGTH_LONG);
    }

    protected void showShortSnackBar(@NonNull View parentView, CharSequence msg) {
        showSnackBar(parentView, msg, Snackbar.LENGTH_SHORT);
    }

    protected void showSnackBar(@NonNull View parentView, CharSequence msg, int duration) {
        if (mSnackBar == null) {
            mSnackBar = Snackbar.make(parentView, msg, duration);
            mSnackBar.show();
        } else if (!mSnackBar.isShown()) {
            mSnackBar.setText(msg).setDuration(duration).show();
        }
    }


    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void hideInputDialog() {
        if (mInputDialog != null && mInputDialog.isShowing()) {
            mInputDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyDialog();
    }

    private void hideDialog() {
        hideProgressDialog();
        hideInputDialog();
    }

    private void destroyDialog() {
        hideDialog();
        mProgressDialog = null;
        mInputDialog = null;
    }
}
