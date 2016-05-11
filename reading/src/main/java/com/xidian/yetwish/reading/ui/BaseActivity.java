package com.xidian.yetwish.reading.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.primitives.Chars;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;


/**
 * base activity,dialog and something
 * Created by Yetwish on 2016/4/8 0008.
 */
public class BaseActivity extends AppCompatActivity {

    private MaterialDialog mProgressDialog;

    private MaterialDialog mInputDialog;

    private Snackbar mSnackBar;

    private MaterialDialog mBasicDialog;

    private @ColorInt int mDialogBgColor;
    private @ColorInt int mDialogTitleTextColor;
    private @ColorInt int mDialogContentTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogBgColor = ContextCompat.getColor(this, R.color.colorWhite);
        mDialogTitleTextColor = ContextCompat.getColor(this, R.color.colorPrimary);
        mDialogContentTextColor = ContextCompat.getColor(this, R.color.colorText);
    }

    protected void drawStatusBar() {
        int statusColor = ContextCompat.getColor(this, R.color.colorPrimary);
        drawStatusBar(statusColor);
    }

    protected void drawStatusBar(int statusBarColor) {
        if (Build.VERSION.SDK_INT < 19) //在Android 4.4之后才可以定制
            return;
        Window window = this.getWindow();
        ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);

        if (Build.VERSION.SDK_INT >= 21) {//Android 5.0之后
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(statusBarColor);

            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        } else {// 4.4 <= Version < 5.0  实质是顶替掉系统的状态栏
            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = ScreenUtils.getStatusHeight(this);

            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                //如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
                if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                    //不预留系统空间
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    lp.topMargin += statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
                //避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(statusBarColor);
                return;
            }
            statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(statusBarColor);
            //向 ContentView 中添加假 View
            mContentView.addView(statusBarView, 0, lp);
        }
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
                    .backgroundColor(mDialogBgColor)
                    .contentColor(mDialogTitleTextColor)
                    .canceledOnTouchOutside(cancelable)
                    .dismissListener(listener)
                    .show();
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.setContent(msg);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setOnDismissListener(listener);
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
                    .backgroundColor(mDialogBgColor)
                    .titleColor(mDialogTitleTextColor)
                    .contentColor(mDialogContentTextColor)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(hintText, preFill, callback)
                    .show();

        } else if (!mInputDialog.isShowing()) {
            mInputDialog.getInputEditText().setText(preFill);
            mInputDialog.show();
        }
    }

    protected void showBasicDialog(CharSequence title, CharSequence content,
                                   MaterialDialog.SingleButtonCallback callback) {
        showBasicDialog(title,content,getString(R.string.confirm),getString(R.string.cancel),callback);
    }


    protected void showBasicDialog(CharSequence title, CharSequence content, CharSequence positiveText,
                                   CharSequence negativeText, final MaterialDialog.SingleButtonCallback callback) {
        mBasicDialog = null;
        mBasicDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .backgroundColor(mDialogBgColor)
                .titleColor(mDialogTitleTextColor)
                .contentColor(mDialogContentTextColor)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        callback.onClick(mBasicDialog,DialogAction.NEGATIVE);
                    }
                })
                .onAny(callback).show();
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

    protected void hideBasicDialog() {
        if (mBasicDialog != null && mBasicDialog.isShowing()) {
            mBasicDialog.dismiss();
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
        hideBasicDialog();
    }

    private void destroyDialog() {
        hideDialog();
        mProgressDialog = null;
        mInputDialog = null;
        mBasicDialog = null;
    }
}
