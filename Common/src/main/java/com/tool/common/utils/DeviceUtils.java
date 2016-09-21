package com.tool.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.tool.common.utils.base.BaseUtils;

import java.lang.reflect.Field;

/**
 * 设备信息工具类
 */
public class DeviceUtils extends BaseUtils {

    public DeviceUtils() {
        super();
    }

    /**
     * 判断是否存在sd卡
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 屏幕高度
     *
     * @param context
     * @return
     */
    public static float getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 屏幕宽度
     *
     * @param context
     * @return
     */
    public static float getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dpToPixel(Context context, float dp) {
        return dp * (getDisplayMetrics(context).densityDpi / 160F);
    }

    /**
     * px转dp
     *
     * @param context
     * @param f
     * @return
     */
    public static float pixelsToDp(Context context, float f) {
        return f / (getDisplayMetrics(context).densityDpi / 160F);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取DisplayMetrics
     *
     * @param context
     * @return
     */
    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics;
    }

    /**
     * 是否是横屏
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        boolean flag;
        if (context.getResources().getConfiguration().orientation == 2) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 是否是竖屏
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        boolean flag = true;
        if (context.getResources().getConfiguration().orientation != 1) {
            flag = false;
        }
        return flag;
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 当前是否有网络
     *
     * @param context
     * @return
     */
    public static boolean hasInternet(Context context) {
        boolean flag;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null && manager.getActiveNetworkInfo() != null) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断包是否存在
     *
     * @param context
     * @param pckName
     * @return
     */
    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager().getPackageInfo(pckName, 0);
            if (pckInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return false;
    }

    /**
     * 卸载软件
     *
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        if (isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 设备是否有后置or前置相机
     *
     * @param context
     * @param flag    后置or前置
     * @return
     */
    public static boolean hasCamera(Context context, boolean flag) {
        boolean hasCamera = false;
        PackageManager pckMgr = context.getPackageManager();
        if (flag) {
            hasCamera = pckMgr.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        } else {
            hasCamera = pckMgr.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        }
        return hasCamera;
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context, final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

    /**
     * 获取设备IMEI
     *
     * @param context
     */
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
}


