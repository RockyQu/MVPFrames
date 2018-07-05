package me.mvp.frame.di.module;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import me.logg.Logg;
import me.logg.config.LoggConfiguration;
import me.mvp.frame.http.BaseUrl;
import me.mvp.frame.http.NetworkInterceptorHandler;
import me.mvp.frame.http.cookie.PersistentCookieJar;
import me.mvp.frame.http.interceptor.NetworkInterceptor;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.integration.cache.CacheType;
import me.mvp.frame.integration.cache.LruCache;
import me.mvp.frame.utils.FileUtils;
import me.mvp.frame.widget.imageloader.BaseImageLoader;
import me.mvp.frame.widget.imageloader.glide.GlideImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * 全局配置信息
 * <p>
 * {@link Builder}
 */
@Module
public class AppConfigModule {

    private HttpUrl httpUrl;
    private BaseUrl baseUrl;

    private File cacheFile;
    private NetworkInterceptorHandler networkInterceptorHandler;
    private List<Interceptor> interceptors;

    private BaseImageLoader imageLoader;

    private HttpModule.RetrofitConfiguration retrofitConfiguration;
    private HttpModule.OkHttpConfiguration okHttpConfiguration;
    private HttpModule.RxCacheConfiguration rxCacheConfiguration;

    private AppModule.GsonConfiguration gsonConfiguration;

    private AppModule.GlobalErrorHandler globalErrorHandler;

    private CookieJar cookie;
    private PersistentCookieJar.CookieLoadForRequest cookieLoadForRequest;

    private Cache.Factory cacheFactory;

    private NetworkInterceptor.Level httpLogLevel;

    public AppConfigModule(Builder buidler) {
        this.httpUrl = buidler.httpUrl;
        this.baseUrl = buidler.baseUrl;

        this.cacheFile = buidler.cacheFile;
        this.networkInterceptorHandler = buidler.networkInterceptorHandler;
        this.interceptors = buidler.interceptors;

        this.imageLoader = buidler.imageLoader;

        this.retrofitConfiguration = buidler.retrofitConfiguration;
        this.okHttpConfiguration = buidler.okHttpConfiguration;
        this.rxCacheConfiguration = buidler.rxCacheConfiguration;

        this.gsonConfiguration = buidler.gsonConfiguration;

        this.globalErrorHandler = buidler.globalErrorHandler;

        this.cacheFactory = buidler.cacheFactory;

        this.cookie = buidler.cookie;
        this.cookieLoadForRequest = buidler.cookieLoadForRequest;

        this.httpLogLevel = buidler.httpLogLevel;

        LoggConfiguration configuration = new LoggConfiguration.Buidler()
                .setDebug(httpLogLevel != NetworkInterceptor.Level.NONE)
                .build();
        Logg.init(configuration);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        if (baseUrl != null) {
            HttpUrl httpUrl = baseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return httpUrl == null ? HttpUrl.parse("https://api.github.com/") : httpUrl;
    }

    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return cacheFile == null ? FileUtils.getCacheFile(application) : cacheFile;
    }

    @Singleton
    @Provides
    NetworkInterceptorHandler provideNetworkHandler() {
        return networkInterceptorHandler == null ? NetworkInterceptorHandler.EMPTY : networkInterceptorHandler;
    }

    @Singleton
    @Provides
    List<Interceptor> provideInterceptors() {
        return interceptors;
    }

    @Singleton
    @Provides
    BaseImageLoader provideBaseImageLoader() {
        return imageLoader == null ? new GlideImageLoader() : imageLoader;
    }

    @Singleton
    @Provides
    HttpModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return retrofitConfiguration == null ? HttpModule.RetrofitConfiguration.EMPTY : retrofitConfiguration;
    }

    @Singleton
    @Provides
    HttpModule.OkHttpConfiguration provideOkHttpConfiguration() {
        return okHttpConfiguration == null ? HttpModule.OkHttpConfiguration.EMPTY : okHttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    HttpModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return rxCacheConfiguration;
    }

    @Singleton
    @Provides
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return gsonConfiguration == null ? AppModule.GsonConfiguration.EMPTY : gsonConfiguration;
    }

    @Singleton
    @Provides
    CookieJar provideCookieJar(Application application) {
        return cookie == null ? new PersistentCookieJar(application, cookieLoadForRequest == null ? PersistentCookieJar.CookieLoadForRequest.EMPTY : cookieLoadForRequest) : cookie;
    }

    @Singleton
    @Provides
    AppModule.GlobalErrorHandler provideErrorConfiguration() {
        return globalErrorHandler == null ? AppModule.GlobalErrorHandler.EMPTY : globalErrorHandler;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(final Application application) {
        return cacheFactory == null ? new Cache.Factory() {

            @NonNull
            @Override
            public Cache build(CacheType type) {
                //若想自定义 LruCache 的 size,或者不想使用 LruCache ,想使用自己自定义的策略
                //请使用 GlobalConfigModule.Builder#cacheFactory() 扩展
                return new LruCache(type.calculateCacheSize(application));
            }
        } : cacheFactory;
    }

    @Singleton
    @Provides
    NetworkInterceptor.Level provideHttpLogLevel() {
        Logg.init(new LoggConfiguration.Buidler()
                .setDebug(httpLogLevel != null && httpLogLevel == NetworkInterceptor.Level.ALL)
                .build());
        return httpLogLevel == null ? NetworkInterceptor.Level.ALL : httpLogLevel;
    }

    /**
     * App 全局配置
     */
    public static class Builder {

        // Http 通信 Base 接口
        private HttpUrl httpUrl;

        /**
         * 针对于 BaseUrl 在 App 启动时不能确定，需要请求服务器接口动态获取的应用场景
         * 在调用 Retrofit 接口之前，使用 Okhttp 或其他方式，请求到正确的 BaseUrl 并通过此方法返回
         *
         * @return
         */
        private BaseUrl baseUrl;

        // Http缓存路径
        private File cacheFile;
        // 网络通信拦截器
        private NetworkInterceptorHandler networkInterceptorHandler;
        // 其他拦截器
        private List<Interceptor> interceptors = new ArrayList<>();

        // 图片框架，默认为Glide
        private BaseImageLoader imageLoader;

        // 提供一个 Retrofit 配置接口，用于对 Retrofit 进行格外的参数配置
        private HttpModule.RetrofitConfiguration retrofitConfiguration;
        // 提供一个 OkHttp 配置接口，用于对 OkHttp 进行格外的参数配置
        private HttpModule.OkHttpConfiguration okHttpConfiguration;
        // 提供一个 RxCache 配置接口，用于对 RxCache 进行格外的参数配置
        private HttpModule.RxCacheConfiguration rxCacheConfiguration;

        // 提供一个 Gson 配置接口，用于对 Gson 进行格外的参数配置
        private AppModule.GsonConfiguration gsonConfiguration;

        // 提供全局错误响应接口，APP的各种错误信息可以统一回调至此接口
        private AppModule.GlobalErrorHandler globalErrorHandler;

        // DB 数据库



        // 框架缓存组件
        private Cache.Factory cacheFactory;

        // OkHttp 用 CookieJar 保持会话功能，框架已集成 PersistentCookieJar 自动管理 Cookie，或自己实现 CookieJar 接口
        private CookieJar cookie;

        // 此接口需求配合 PersistentCookieJar 使用
        // 这是用来从 PersistentCookieJar 的 loadForRequest 获取 List<Cookie>
        // 实际上只是为了获取到接口里的 Cookie 值，如果项目存在两套 Http 模块
        // 比如登录模块用 OkHttp，其他模块需要用到登录模块返回的Cookie来保持会话，此时需要实现此接口将返回的 Cookie 设置给另外一套Http模块
        // 正常情况下一般不会用到
        private PersistentCookieJar.CookieLoadForRequest cookieLoadForRequest;

        // 设置 HTTP 日志级别
        private NetworkInterceptor.Level httpLogLevel;

        private Builder() {
            ;
        }

        public Builder httpUrl(String httpUrl) {
            if (TextUtils.isEmpty(httpUrl)) {
                throw new IllegalArgumentException("httpUrl can not be empty!");
            }
            this.httpUrl = HttpUrl.parse(httpUrl);
            return this;
        }

        public Builder baseurl(BaseUrl baseUrl) {
            if (baseUrl == null) {
                throw new IllegalArgumentException("BaseUrl can not be null");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder networkInterceptorHandler(NetworkInterceptorHandler networkInterceptorHandler) {
            this.networkInterceptorHandler = networkInterceptorHandler;
            return this;
        }

        public Builder interceptors(Interceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        public Builder imageLoader(BaseImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public Builder retrofitConfiguration(HttpModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okHttpConfiguration(HttpModule.OkHttpConfiguration okHttpConfiguration) {
            this.okHttpConfiguration = okHttpConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(HttpModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder globalErrorHandler(AppModule.GlobalErrorHandler globalErrorHandler) {
            this.globalErrorHandler = globalErrorHandler;
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public Builder cookie(CookieJar cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder cookieLoadForRequest(PersistentCookieJar.CookieLoadForRequest cookieLoadForRequest) {
            this.cookieLoadForRequest = cookieLoadForRequest;
            return this;
        }

        public Builder httpLogLevel(NetworkInterceptor.Level httpLogLevel) {
            this.httpLogLevel = httpLogLevel;
            return this;
        }

        public AppConfigModule build() {
            return new AppConfigModule(this);
        }
    }
}