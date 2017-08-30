package com.tool.common.base;

import android.app.Application;

import com.tool.common.base.delegate.AppDelegate;
import com.tool.common.di.component.AppComponent;
import com.tool.common.utils.AppUtils;

/**
 * Source https://github.com/DesignQu/MVPFrames
 */
public abstract class BaseApplication extends Application implements App {

    // AppDelegate
    private AppDelegate delegate;

    @Override
    public void onCreate() {
        super.onCreate();

        // 如果声明了独立进程的Service会导致Application执行多次，这里控制只执行默认主进程
        String processName = AppUtils.getProcessName(this);
        if (!getPackageName().equals(processName)) {
            return;
        }

        this.delegate = new AppDelegate(this);
        this.delegate.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        this.delegate.onTerminate();
    }

    /**
     * 返回AppComponent提供统一出口，AppComponent里拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return delegate.getAppComponent();
    }
}