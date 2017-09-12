package com.tool.common.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;

import com.tool.common.base.App;
import com.tool.common.base.simple.delegate.ISimpleActivity;
import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.IPresenter;
import com.tool.common.utils.AppUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * ActivityDelegateImpl
 */
public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity activity;
    private Unbinder unbinder;

    private IActivity iActivity;
    private ISimpleActivity iSimpleActivity;

    private IPresenter iPresenter;

    public ActivityDelegateImpl(Activity activity) {
        this.activity = activity;
        if (activity instanceof IActivity) {
            this.iActivity = (IActivity) activity;
        } else if (activity instanceof ISimpleActivity) {
            this.iSimpleActivity = (ISimpleActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppComponent component = AppUtils.obtainAppComponentFromContext(activity);
        // 在Base基类实现些方法，为了能够方便的获取到AppComponent
        if (iActivity != null) {
            iActivity.setComponent(component);
        } else if (iSimpleActivity != null) {
            iSimpleActivity.setComponent(component);
        }

        // 依赖注入
        if (iActivity != null) {
            iActivity.setupActivityComponent(component);
        } else if (iSimpleActivity != null) {
            this.iPresenter = iSimpleActivity.obtainPresenter();
            iSimpleActivity.setPresenter(iPresenter);
        }

        try {
            int layoutResID;
            if (iActivity != null) {
                layoutResID = iActivity.getLayoutId();
            } else if (iSimpleActivity != null) {
                layoutResID = iSimpleActivity.getLayoutId();
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
        } else if (iSimpleActivity != null) {
            iSimpleActivity.create(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        // 注册EventBus
        if (iActivity != null) {
            if (iActivity.useEventBus()) {
                EventBus.getDefault().register(activity);
            }
        } else if (iSimpleActivity != null) {
            if (iSimpleActivity.useEventBus()) {
                EventBus.getDefault().register(activity);
            }
        }
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
        } else if (iSimpleActivity != null) {
            if (iSimpleActivity.useEventBus()) {
                EventBus.getDefault().unregister(activity);
            }
        }

        //释放资源
        if (iPresenter != null) {
            iPresenter.onDestroy();
        }

        this.unbinder = null;
        this.activity = null;

        if (iActivity != null) {
            this.iActivity = null;
        }
        if (iSimpleActivity != null) {
            this.iSimpleActivity = null;
        }
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
        this.unbinder = in.readParcelable(Unbinder.class.getClassLoader());

        if (iActivity != null) {
            this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
        }
        if (iSimpleActivity != null) {
            this.iSimpleActivity = in.readParcelable(ISimpleActivity.class.getClassLoader());
        }

        this.iPresenter = in.readParcelable(IPresenter.class.getClassLoader());
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