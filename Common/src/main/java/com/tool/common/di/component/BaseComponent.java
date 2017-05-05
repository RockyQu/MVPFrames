package com.tool.common.di.component;

import android.app.Application;

import com.google.gson.Gson;
import com.tool.common.base.delegate.AppDelegateManager;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.di.module.ImageModule;
import com.tool.common.integration.IRepositoryManager;
import com.tool.common.widget.imageloader.ImageLoader;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * BaseComponent
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class, ImageModule.class, AppConfigModule.class})
public interface BaseComponent {

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

    void inject(AppDelegateManager delegate);
}