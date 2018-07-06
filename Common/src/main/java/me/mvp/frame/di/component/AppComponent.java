package me.mvp.frame.di.component;

import android.app.Application;

import com.google.gson.Gson;

import me.mvp.frame.base.delegate.ApplicationDelegate;
import me.mvp.frame.db.DBManager;
import me.mvp.frame.di.module.AppConfigModule;
import me.mvp.frame.di.module.AppModule;
import me.mvp.frame.di.module.DBModule;
import me.mvp.frame.di.module.HttpModule;
import me.mvp.frame.integration.AppManager;
import me.mvp.frame.integration.IRepositoryManager;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.widget.imageloader.ImageLoader;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * BaseComponent
 */
@Singleton
@Component(modules = {AppModule.class, HttpModule.class, DBModule.class, AppConfigModule.class})
public interface AppComponent {

    // Application
    Application getApplication();

    // 用来管理网络请求层,以及数据缓存层
    IRepositoryManager getRepositoryManager();

    // Gson
    Gson getGson();

    // OkHttpClient
    OkHttpClient getOkHttpClient();

    // Global Error
    AppModule.GlobalErrorHandler getGlobalErrorHandler();

    // 图片框架
    ImageLoader getImageLoader();

    // 缓存文件根目录，应该将所有缓存放到这个根目录里，便于管理和清理，可在AppConfigModule里配置
    File getCacheFile();

    // 管理所有Activity
    AppManager appManager();

    // 数据库
    DBManager dbManager();

    // 用来存取一些 App 公用数据，切勿大量存放大容量数据
    Cache<String, Object> extras();

    // 缓存对象的工厂
    Cache.Factory cacheFactory();

    void inject(ApplicationDelegate delegate);
}