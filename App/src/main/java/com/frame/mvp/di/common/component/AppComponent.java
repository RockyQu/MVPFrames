package com.frame.mvp.di.common.component;

import android.app.Application;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.di.common.module.DBModule;
import com.frame.mvp.entity.DaoSession;
import com.google.gson.Gson;
import com.tool.common.di.component.BaseComponent;
import com.tool.common.di.scope.ApplicationScope;
import com.tool.common.integration.IRepositoryManager;
import com.tool.common.widget.imageloader.ImageLoader;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * AppComponent
 */
@ApplicationScope
@Component(modules = {DBModule.class},dependencies = BaseComponent.class)
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