package com.tool.common.base;

import android.app.Application;
import android.content.Context;

/**
 * 项目介绍及使用
 * <p>
 * 项目涉及框架
 * 1、ButterKnife 注解框架 http://jakewharton.github.io/butterknife/
 * 2、
 * 3、
 * 4、
 * 5、
 * 6、
 * <p>
 * 使用方法
 * 1、配置自定义Application必须继承BaseApplication，BaseApplication完成Http框架、图片框架、日志管理框架等初始化工作。
 * 2、
 * 3、
 * 4、
 * 5、
 * 6、
 */
public abstract class BaseApplication extends Application {

    // Context
    private static BaseApplication application = null;

    @Override
    public void onCreate() {
        super.onCreate();

        this.application = this;

        if (Config.Debug) {// 日志管理
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
