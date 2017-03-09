package com.tool.common.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.http.receiver.NetworkStatusReceiver;
import com.tool.common.log.log.LogConfig;
import com.tool.common.di.module.ImageModule;

/**
 * 一、项目涉及的主要框架
 * 1、Dagger2 https://google.github.io/dagger
 * 2、ButterKnife http://jakewharton.github.io/butterknife
 * 3、Retrofit2 https://github.com/square/retrofit
 * 4、Glide https://github.com/bumptech/glide
 * 5、Gson https://github.com/google/gson
 * <p>
 * 二、基本使用方法
 * 1、配置自定义Application必须继承BaseApplication，BaseApplication完成Http框架、图片框架、日志管理框架等初始化工作
 * 2、使用Activity、Fragment、ViewHolder、Service、Adapter请继承CommonActivity、CommonFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构
 * 3、通过Application获取AppComponent里面的对象可直接使用
 * 4、简单功能及页面无需引入MVP
 * <p>
 * 三、未来可能会更新的一些功能
 * 1、路由框架
 * 2、用户行为分析日志模块
 * 3、优化MVP缺点（类、接口过多的问题）
 * 4、重构蓝牙定位模块
 * 5、数据库模块
 * 6、混淆
 */
public abstract class BaseApplication extends Application {

    // Context
    private static Context context;

    // App Config
    protected AppConfigModule appConfiguration;

    // AppModule
    private AppModule appModule;
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

        // Log配置
        this.logConfig = LogConfig.Buidler
                .buidler()
                .setContext(this)
                .setOpen(logSwitch())
                .build();

        // 提供Application、Gson
        this.appModule = new AppModule(this);
        // Http模块
        this.httpModule = new HttpModule();
        // 图片模块
        this.imageModule = new ImageModule();

        // App Config
        this.appConfiguration = getAppConfiguration();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (context != null) {
            this.context = this;
        }
        if (appModule != null) {
            this.appModule = null;
        }
        if (httpModule != null) {
            this.httpModule = null;
        }
        if (imageModule != null) {
            this.imageModule = null;
        }
        if (appConfiguration != null) {
            this.appConfiguration = null;
        }
        if (logConfig != null) {
            this.logConfig = null;
        }
    }

    public static Context getContext() {
        return context;
    }

    public AppModule getAppModule() {
        return appModule;
    }

    public HttpModule getHttpModule() {
        return httpModule;
    }

    public ImageModule getImageModule() {
        return imageModule;
    }

    /**
     * 日志开关
     *
     * @return AppConfiguration
     */
    protected abstract boolean logSwitch();

    /**
     * App全局配置信息
     *
     * @return AppConfiguration
     */
    protected abstract AppConfigModule getAppConfiguration();
}