package me.mvp.frame.base.delegate.activity;

import android.app.Activity;
import android.os.Bundle;

import me.mvp.frame.base.IActivity;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.IPresenter;
import me.mvp.frame.utils.AppUtils;

import org.simple.eventbus.EventBus;

/**
 * {@link ActivityDelegate} Activity 生命周期代理实现类
 */
public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity activity;
    private IActivity iActivity;
    private IPresenter iPresenter;

    public ActivityDelegateImpl(Activity activity) {
        this.activity = activity;
        if (activity instanceof IActivity) {
            this.iActivity = (IActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 注册 EventBus
        if (iActivity != null) {
            if (iActivity.useEventBus()) {
                EventBus.getDefault().register(activity);
            }
        }

        AppComponent component = AppUtils.obtainAppComponentFromContext(activity);

        // 在 Base 基类实现些方法，为了能够方便的获取到 AppComponent
        if (iActivity != null) {
            iActivity.setComponent(component);
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        // 注销EventBus
        if (iActivity != null) {
            if (iActivity.useEventBus()) {
                EventBus.getDefault().unregister(activity);
            }
        }

        this.activity = null;
        this.iActivity = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {

    }
}