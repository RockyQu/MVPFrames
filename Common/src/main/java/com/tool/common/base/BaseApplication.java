package com.tool.common.base;

import android.app.Application;
import android.content.Context;

/**
 * Application
 */
public abstract class BaseApplication extends Application {

    // Context
    private static BaseApplication application = null;

    @Override
    public void onCreate() {
        super.onCreate();

        this.application = this;

        if (Config.Debug) {// 日志记录
            //TODO
        }
        if (Config.MemoryLeak) {// 内存泄露
            //TODO
        }
    }

    /**
     * 返回上下文
     *
     * @return Context
     */
    public static Context getContext() {
        return application;
    }

    /**
     * 获取Root API
     *
     * @return
     */
    protected abstract String getJavaUrl();
}
