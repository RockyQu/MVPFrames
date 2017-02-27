package com.frame.mvp.di.component;

import android.app.Application;

import com.frame.mvp.app.api.service.ServiceManager;
import com.frame.mvp.di.module.ApiModule;
import com.google.gson.Gson;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.widget.imageloader.ImageLoader;
import com.tool.common.di.module.ImageModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * AppComponent
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class, ApiModule.class, ImageModule.class, AppConfigModule.class})
public interface AppComponent {

    // Application
    Application getApplication();

    // Gson
    Gson getGson();

    // OkHttpClient
    OkHttpClient getOkHttpClient();

    // Retrofit
    Retrofit getRetrofit();

    // 图片框架
    ImageLoader getImageLoader();

    // Api管理器
    ServiceManager getServiceManager();
}