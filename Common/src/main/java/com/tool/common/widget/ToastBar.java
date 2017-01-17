package com.tool.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;

import com.tool.common.base.BaseActivity;
import com.tool.common.base.BaseApplication;

/**
 * ToastBar
 */
public class ToastBar {

    public static Context getContext() {
        return BaseApplication.getContext();
    }

    public static void show(Activity context, String message) {
        Snackbar.make(context.getWindow().getDecorView(), message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 显示提示信息框
     *
     * @param message
     */
    public static void show(String message) {
        Intent intent = new Intent(BaseActivity.RECEIVER_ACTION);
        intent.putExtra(BaseActivity.RECEIVER_TYPE, BaseActivity.RECEIVER_TOAST);
        intent.putExtra(BaseActivity.RECEIVER_MESSAGE, message);
        getContext().sendBroadcast(intent);
    }
}