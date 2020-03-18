package com.codevian.campgladiator.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.codevian.campgladiator.R;


public class ProgressDialog {
    private Dialog dialog;
    private static ProgressDialog mInstance;

    public static synchronized ProgressDialog getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressDialog();
        }
        return mInstance;
    }

    public void show(Context context) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_loading);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}