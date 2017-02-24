package com.tool.common.base;

import android.app.Application;
import android.content.Context;

import com.tool.common.di.module.HttpModule;
import com.tool.common.log.log.LogConfig;
import com.tool.common.di.module.ImageModule;

/**
 * 项目介绍及使用
 * <p>
 * 项目涉及的主要框架
 * 1、Dagger2 https://google.github.io/dagger
 * 2、ButterKnife http://jakewharton.github.io/butterknife
 * 3、Retrofit2 https://github.com/square/retrofit
 * 4、Glide https://github.com/bumptech/glide
 * 5、Gson https://github.com/google/gson
 * <p>
 * 使用方法
 * 1、配置自定义Application必须继承BaseApplication，BaseApplication完成Http框架、图片框架、日志管理框架等初始化工作
 * 2、使用Activity、Fragment、ViewHolder、Service、Adapter请继承CommonActivity、CommonFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构
 * 3、简单功能及页面无需引入MVP
 */
public abstract class BaseApplication extends Application {

    // Context
    private static Context context;

    // App Config
    protected AppConfiguration appConfiguration;

    // Http模块
    private HttpModule httpModule;
    // 图片模块
    private ImageModule imageModule;

    // Log配置
    private LogConfig logConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = this;

        // App Config
        this.appConfiguration = getAppConfiguration();

        // Http模块
        this.httpModule = HttpModule.Buidler
                .buidler()
                .url(appConfiguration.getHttpUrl())
                .cacheFile(appConfiguration.getHttpCacheFile())
                .networkInterceptor(appConfiguration.getNetworkInterceptor())
                .interceptors(appConfiguration.getInterceptors())
                .build();

        // 图片模块
        this.imageModule = new ImageModule();

        // Log配置
        this.logConfig = LogConfig.Buidler
                .buidler()
                .setContext(this)
                .setOpen(appConfiguration.isDebug())
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (httpModule != null) {
            httpModule = null;
        }
        if (imageModule != null) {
            imageModule = null;
        }
        if (appConfiguration != null) {
            appConfiguration = null;
        }
        if (context != null) {
            context = null;
        }
        if (logConfig != null) {
            logConfig = null;
        }
    }

    public static Context getContext() {
        return context;
    }

    public HttpModule getHttpModule() {
        return httpModule;
    }

    public ImageModule getImageModule() {
        return imageModule;
    }

    /**
     * App全局配置信息
     *
     * @return AppConfiguration
     */
    protected abstract AppConfiguration getAppConfiguration();
}