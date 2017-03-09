package com.tool.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.tool.common.utils.base.BaseUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * KnifeUtils
 */
public class KnifeUtils extends BaseUtils {

    public KnifeUtils() {
        super();
    }

    public static Unbinder bindTarget(Object target, Object source) {
        if (source instanceof Activity) {
            return ButterKnife.bind(target, (Activity) source);
        } else if (source instanceof View) {
            return ButterKnife.bind(target, (View) source);
        } else if (source instanceof Dialog) {
            return ButterKnife.bind(target, (Dialog) source);
        } else {
            return Unbinder.EMPTY;
        }
    }
}