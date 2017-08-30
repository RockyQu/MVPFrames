package com.tool.common.di.component;

import android.app.Application;

import com.google.gson.Gson;
import com.tool.common.base.delegate.AppDelegate;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.integration.AppManager;
import com.tool.common.integration.IRepositoryManager;
import com.tool.common.widget.imageloader.ImageLoader;

import java.io.File;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * BaseComponent
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class, AppConfigModule.class})
public interface AppComponent {

    // Application
    Application getApplication();

    // 用来管理网络请求层,以及数据缓存层
    IRepositoryManager getRepositoryManager();

    // Gson
    Gson getGson();

    // OkHttpClient
    OkHttpClient getOkHttpClient();

    // 图片框架
    ImageLoader getImageLoader();

    // 缓存文件根目录，应该将所有缓存放到这个根目录里，便于管理和清理，可在AppConfigModule里配置
    File getCacheFile();

    // 管理所有Activity
    AppManager appManager();

    // 用来存取一些App公用数据，切勿大量存放大容量数据
    Map<String, Object> extras();

    void inject(AppDelegate delegate);
}