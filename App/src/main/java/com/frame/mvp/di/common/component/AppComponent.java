package com.frame.mvp.di.common.component;

import android.app.Application;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.di.common.module.DBModule;
import com.frame.mvp.entity.DaoSession;
import com.google.gson.Gson;
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
 * AppComponent
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class, ImageModule.class, DBModule.class,AppConfigModule.class})
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

    // 数据库
    DaoSession getDaoSession();

    void inject(MVPApplication application);
}