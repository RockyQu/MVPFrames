package me.mvp.frame.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import me.mvp.frame.R;
import me.mvp.frame.integration.AppManager;

import org.simple.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * 提示框
 */
public class Toaster implements View.OnClickListener {

    private WeakReference<Activity> activityWeakReference;

    // 显示时长
    @Duration
    private int duration = Toast.LENGTH_LONG;

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    @interface Duration {
    }

    // 提示文字信息
    private String message = "";

    // Toast
    private Toast toast;

    private Toaster(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);

        toast = new Toast(getContext());

        setDefault();
    }

    /**
     * Create Toaster with activity reference
     *
     * @param activity
     * @return Toaster
     */
    public static Toaster with(Activity activity) {
        return new Toaster(activity);
    }

    /**
     * Sets the default values to the Toaster
     */
    private void setDefault() {
        this.duration = Toast.LENGTH_LONG;

        this.message = "";
    }

    /**
     * Return Context
     *
     * @return Context
     */
    private Context getContext() {
        return activityWeakReference.get();
    }

    /**
     * 设置显示时长
     *
     * @param duration
     * @return Toaster
     */
    public Toaster setDuration(@Duration int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * 设置提示信息文字
     *
     * @param message
     * @return Toaster
     */
    public Toaster setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 显示提示框，请在最后调用
     */
    public void show() {
        this.createView().show();
    }

    /**
     * 移除
     */
    @Deprecated
    private void hide() {

    }

    /**
     * 创建并添加提示框
     *
     * @return Toast
     */
    private Toast createView() {
        TextView view = new TextView(getContext());
        view.setOnClickListener(this);
        view.setBackgroundResource(R.drawable.bg_toaster);
        view.setGravity(Gravity.CENTER);
        view.setText(message);
        view.setTextColor(Color.parseColor("#FFFFFF"));
        toast.setView(view);
        return toast;
    }

    /**
     * 点击关闭
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        this.hide();
    }

    /**
     * 通过EventBus远程遥控显示 {@link Toaster}
     *
     * @param message
     */
    public static void postMessage(String message) {
        Message msg = new Message();
        msg.what = AppManager.TOASTER;
        msg.obj = message;
        EventBus.getDefault().post(msg, AppManager.APPMANAGER_MESSAGE);
    }
}