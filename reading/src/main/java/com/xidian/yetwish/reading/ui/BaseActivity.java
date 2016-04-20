package com.xidian.yetwish.reading.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.ui.dialog.DialogManager;

/**
 * base activity,dialog and something
 * Created by Yetwish on 2016/4/8 0008.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

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

    protected void showProgressDialog(String info) {
        DialogManager.getInstance().showProgressDialog(this, info);
//        showProgressDialog(info, true);
    }

    protected void showProgressDialog(String info, boolean cancelable) {
        showProgressDialog("", info, cancelable);
    }

    protected void showProgressDialog(String title, String info,
                                      boolean cancelable) {
        if (!isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(this, title, info, true,
                        cancelable, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                mProgressDialog.dismiss();
                            }
                        });
            } else {
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }

        }
    }

    protected void hideDialog(){
        DialogManager.getInstance().hideDialog();
    }

}
