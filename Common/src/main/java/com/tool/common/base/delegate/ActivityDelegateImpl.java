package com.tool.common.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;

import com.tool.common.base.App;
import com.tool.common.log.QLog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ActivityDelegateImpl
 */
public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity activity;
    private IActivity iActivity;
    private Unbinder unbinder;

    public ActivityDelegateImpl(Activity activity) {
        this.activity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // 在Base基类实现些方法，为了能够方便的获取到AppComponent
        iActivity.setComponent(((App) activity.getApplication()).getAppComponent());

        // 依赖注入
        iActivity.setupActivityComponent(((App) activity.getApplication()).getAppComponent());

        try {
            int layoutResID = iActivity.getLayoutId();
            if (layoutResID != 0) {
                activity.setContentView(layoutResID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 绑定ButterKnife
        unbinder = ButterKnife.bind(activity);

        // 初始化方法
        iActivity.create(savedInstanceState);
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

        this.unbinder = null;
        this.iActivity = null;
        this.activity = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected ActivityDelegateImpl(Parcel in) {
        this.activity = in.readParcelable(Activity.class.getClassLoader());
        this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
        this.unbinder = in.readParcelable(Unbinder.class.getClassLoader());
    }

    public static final Creator<ActivityDelegateImpl> CREATOR = new Creator<ActivityDelegateImpl>() {

        @Override
        public ActivityDelegateImpl createFromParcel(Parcel source) {
            return new ActivityDelegateImpl(source);
        }

        @Override
        public ActivityDelegateImpl[] newArray(int size) {
            return new ActivityDelegateImpl[size];
        }
    };
}