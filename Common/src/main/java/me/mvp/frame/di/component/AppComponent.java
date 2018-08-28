package me.mvp.frame.di.component;

import android.app.Application;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.google.gson.Gson;

import me.mvp.frame.base.delegate.ApplicationDelegate;
import me.mvp.frame.db.DBManager;
import me.mvp.frame.di.module.AppConfigModule;
import me.mvp.frame.di.module.AppModule;
import me.mvp.frame.di.module.DBModule;
import me.mvp.frame.di.module.HttpModule;
import me.mvp.frame.integration.AppManager;
import me.mvp.frame.integration.ConfigModule;
import me.mvp.frame.integration.IRepositoryManager;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.utils.AppUtils;
import me.mvp.frame.widget.imageloader.BaseImageLoaderStrategy;
import me.mvp.frame.widget.imageloader.ImageLoader;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * 此接口里的实例全部可以直接引用，通过 {@link AppUtils#obtainAppComponentFromContext(Context)} 获取
 */
@Singleton
@Component(modules = {
        AppModule.class,
        HttpModule.class,
        DBModule.class,
        AppConfigModule.class
})
public interface AppComponent {

    Application getApplication();

    /**
     * 用于管理网络请求层, 以及数据缓存层
     *
     * @return {@link IRepositoryManager}
     */
    IRepositoryManager getRepositoryManager();

    /**
     * Json 序列化
     *
     * @return {@link Gson}
     */
    Gson getGson();

    /**
     * 网络请求框架
     *
     * @return {@link OkHttpClient}
     */
    OkHttpClient getOkHttpClient();

    /**
     * 全局异常回调
     *
     * @return {@link AppModule.GlobalErrorHandler}
     */
    AppModule.GlobalErrorHandler getGlobalErrorHandler();

    /**
     * 图片加载管理器, 用于加载图片的管理类, 使用策略者模式, 可在运行时动态替换任何图片加载框架
     * arms-imageloader-glide 提供 Glide 的策略实现类, 也可以自行实现
     * 需要在 {@link ConfigModule#applyOptions(Context, AppConfigModule.Builder)} 中
     * 手动注册 {@link BaseImageLoaderStrategy}, {@link ImageLoader} 才能正常使用
     *
     * @return
     */
    ImageLoader getImageLoader();

    /**
     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
     * 便于管理和清理, 可在 {@link ConfigModule#applyOptions(Context, AppConfigModule.Builder)} 种配置
     *
     * @return {@link File}
     */
    File getCacheFile();

    // 管理所有Activity
    AppManager appManager();

    /**
     * 使用 2017 Google IO 大会 Architecture Components 架构组件 Room
     * <p>
     * 请在外部 Module 添加自己的数据库并继承 {@link RoomDatabase} ，调用 {@link AppComponent#dbManager()#database()}
     * 方法获取实例，{@link RoomDatabase} 已经实现单例化，外部无需再处理
     *
     * @return {@link DBManager}
     */
    DBManager dbManager();

    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 {@link Application} 的生命周期一致
     *
     * @return {@link Cache}
     */
    Cache<String, Object> extras();

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return {@link Cache.Factory}
     */
    Cache.Factory cacheFactory();

    void inject(ApplicationDelegate delegate);
}