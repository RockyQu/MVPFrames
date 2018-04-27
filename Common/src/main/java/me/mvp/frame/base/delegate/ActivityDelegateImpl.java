package me.mvp.frame.base.delegate;

import android.app.Activity;
import android.os.Bundle;

import me.mvp.frame.base.IActivity;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.IPresenter;
import me.mvp.frame.utils.AppUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link ActivityDelegate} Activity 生命周期代理实现类
 */
public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity activity;
    private Unbinder unbinder;

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
        // 注册EventBus
        if (iActivity != null) {
            if (iActivity.useEventBus()) {
                EventBus.getDefault().register(activity);
            }
        }

        AppComponent component = AppUtils.obtainAppComponentFromContext(activity);

        // 在Base基类实现些方法，为了能够方便的获取到AppComponent
        if (iActivity != null) {
            iActivity.setComponent(component);
        }

        // 依赖注入
        if (iActivity != null) {
            this.iPresenter = iActivity.obtainPresenter();
            iActivity.setPresenter(iPresenter);
        }

        try {
            int layoutResID;
            if (iActivity != null) {
                layoutResID = iActivity.getLayoutId();
            } else {
                layoutResID = 0;
            }

            if (layoutResID != 0) {
                activity.setContentView(layoutResID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 绑定ButterKnife
        unbinder = ButterKnife.bind(activity);

        // 初始化方法
        if (iActivity != null) {
            iActivity.create(savedInstanceState);
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
        // 解除绑定
        if (unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }

        // 注销EventBus
        if (iActivity != null) {
            if (iActivity.useEventBus()) {
                EventBus.getDefault().unregister(activity);
            }
        }

        // 释放资源
        if (iPresenter != null) {
            iPresenter.onDestroy();
        }

        this.unbinder = null;
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