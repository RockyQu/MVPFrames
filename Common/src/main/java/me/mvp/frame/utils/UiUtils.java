package me.mvp.frame.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import me.mvp.frame.utils.base.BaseUtils;

/**
 * UiUtils
 */
public class UiUtils extends BaseUtils {

    private UiUtils() {
        super();
    }

    /**
     * Get LayoutInflater View
     *
     * @param context
     * @param layout
     * @param parentView
     */
    public static View getView(Activity context, int layout, View parentView) {
        return context.getLayoutInflater().inflate(layout, (ViewGroup) parentView.getParent(), false);
    }
}
