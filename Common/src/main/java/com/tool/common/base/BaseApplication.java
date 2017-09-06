package com.tool.common.base;

import android.app.Application;
import android.content.Context;

import com.tool.common.base.delegate.AppDelegate;
import com.tool.common.di.component.AppComponent;
import com.tool.common.utils.AppUtils;

/**
 * Source https://github.com/DesignQu/MVPFrames
 */
public abstract class BaseApplication extends Application implements App {

    // AppDelegate
    private AppDelegate delegate;

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // 如果声明了独立进程的Service会导致Application执行多次，这里控制只执行默认主进程
        // 此处为临时修改，如果涉及组件化，多进程开发，这处应去掉
        String processName = AppUtils.getProcessName(this);
        if (!getPackageName().equals(processName)) {
            return;
        }

        this.delegate = new AppDelegate(base);
        this.delegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.delegate.onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        this.delegate.onTerminate(this);
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