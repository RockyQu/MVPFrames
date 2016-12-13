package com.tool.common.base;

import android.app.Application;
import android.content.Context;

/**
 * 项目介绍及使用
 * <p>
 * 项目用到的主要框架
 * 1、ButterKnife http://jakewharton.github.io/butterknife/
 * 2、Retrofit2 https://github.com/square/retrofit
 * <p>
 * 使用方法
 * 1、配置自定义Application必须继承BaseApplication，BaseApplication完成Http框架、图片框架、日志管理框架等初始化工作
 * 2、使用Activity、Fragment、ViewHolder、Service、Adapter请继承BaseActivity、BaseFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构
 * 3、简单功能及页面无需引入MVP
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
}
