package com.frame.mvp.app;

import android.content.Context;

import com.frame.mvp.BuildConfig;
import com.frame.mvp.app.api.Api;
import com.frame.mvp.di.common.component.AppComponent;
import com.frame.mvp.di.common.component.DaggerAppComponent;
import com.frame.mvp.di.common.module.DBModule;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tool.common.base.BaseApplication;
import com.tool.common.http.receiver.NetworkStatusReceiver;
import com.tool.common.log.QLog;
import com.tool.common.log.crash.ThreadCatchInterceptor;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;
import com.tool.common.utils.ProjectUtils;

/**
 * Application
 */
public class MVPApplication extends BaseApplication {

    // AppComponent
    private AppComponent appComponent;

    // 当前登录用户信息
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .dBModule(new DBModule())
                .baseComponent(getBaseComponent())
                .build();
        appComponent.inject(this);

        if (ProjectUtils.init()) {
            // 设置反馈崩溃信息，不需要可以不设置
            ThreadCatchInterceptor.getInstance().install(new ThreadCatchInterceptor.CallBack() {

                @Override
                public void error(Throwable throwable) {
                    ;
                }

                @Override
                public void finish(String path) {
                    QLog.i(path);
                }
            });
        } else {
            // 初始化失败
        }

        // 读取当前登录用户信息
        String value = PreferencesUtils.getString(this, LoginActivity.FLAG_USER, null);
        if (value != null) {
            user = GsonUtils.getEntity(value, User.class);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * 获得当前网络状态
     * <p>
     * NONE(1) 无网络，MOBILE(2)移动网络，WIFI(4)无线网络
     *
     * @return
     */
    public static NetworkStatusReceiver.Type getNetworkType() {
        return NetworkStatusReceiver.getType(getContext());
    }

    @Override
    protected boolean logSwitch() {
        return BuildConfig.DEBUG;
    }

    /**
     * 返回AppComponent提供统一出口，AppComponent里拿到对象后都可以直接使用。
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}