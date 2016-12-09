package com.frame.mvp.ui.widget;

import android.app.Activity;
import android.support.design.widget.Snackbar;

/**
 * ToastBar
 */
public class ToastBar {

    public static void show(Activity context, String message) {
        Snackbar.make(context.getWindow().getDecorView(), message, Snackbar.LENGTH_LONG).show();
    }
}