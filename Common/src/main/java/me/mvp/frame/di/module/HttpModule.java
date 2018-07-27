package me.mvp.frame.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import me.mvp.frame.http.NetworkInterceptorHandler;
import me.mvp.frame.http.interceptor.NetworkInterceptor;
import me.mvp.frame.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * HTTP 参数配置
 */
@Module
public class HttpModule {

    // 连接、读取、写入超时时间
    private static final int TIME_OUT = 60;
    // 缓存文件最大值100MB
    private static final int CACHE_MAX_SIZE = 1024 * 1024 * 10 * 100;

    @Singleton
    @Provides
    Retrofit provideRetrofit(Application application, HttpModule.RetrofitConfiguration retrofitConfiguration, Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl) {
        builder
                .baseUrl(httpUrl)// 域名
                .client(client);// 设置OkHttpClient

        builder
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());// 支持 RxJava

        retrofitConfiguration.configRetrofit(application, builder);

        return builder.build();
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(@Nullable Application application, @Nullable HttpModule.OkHttpConfiguration okHttpConfiguration, @Nullable OkHttpClient.Builder okHttpClient, @Nullable Interceptor interceptor,
                               final NetworkInterceptorHandler handler, List<Interceptor> interceptors, CookieJar cookie) {
        OkHttpClient.Builder builder = okHttpClient
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)// 设置出现错误进行重新连接
                .cache(new Cache(application.getCacheDir(), CACHE_MAX_SIZE))// 设置缓存路径和大小
                .addNetworkInterceptor(interceptor);// 网络拦截器

        if (handler != null) {
            okHttpClient.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onHttpRequest(chain, chain.request()));
                }
            });
        }

        if (cookie != null) {
            builder.cookieJar(cookie);// Cookie
        }

        if (interceptors != null && interceptors.size() > 0) {// Interceptors，只在Response被调用一次
            for (Interceptor item : interceptors) {
                builder.addInterceptor(item);
            }
        }

        if (okHttpConfiguration != null) {
            okHttpConfiguration.configOkHttp(application, builder);
        }

        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    /**
     * Http 拦截器
     */
    @Singleton
    @Provides
    Interceptor provideNetworkInterceptor(NetworkInterceptor interceptor) {
        return interceptor;
    }

    /**
     * 提供 RxCache
     *
     * @param cacheDirectory RxCache 缓存路径
     * @return
     */
    @Singleton
    @Provides
    RxCache provideRxCache(Application application, @Nullable RxCacheConfiguration configuration, @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();

        RxCache rxCache = null;
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder);
        }

        if (rxCache != null) {
            return rxCache;
        }

        return builder.persistence(cacheDirectory, new GsonSpeaker());
    }


    /**
     * 需要单独给 RxCache 提供缓存路径
     * 提供 RxCache 缓存地址
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        FileUtils.createOrExistsDir(cacheDirectory);
        return cacheDirectory;
    }

    /**
     * 提供一个 Retrofit 配置接口，用于对 Retrofit 进行格外的参数配置
     */
    public interface RetrofitConfiguration {

        void configRetrofit(Context context, Retrofit.Builder builder);

        RetrofitConfiguration EMPTY = new RetrofitConfiguration() {

            @Override
            public void configRetrofit(Context context, Retrofit.Builder builder) {

            }
        };
    }

    /**
     * 提供一个 OkHttp 配置接口，用于对 OkHttp 进行格外的参数配置
     */
    public interface OkHttpConfiguration {

        void configOkHttp(Context context, OkHttpClient.Builder builder);

        OkHttpConfiguration EMPTY = new OkHttpConfiguration() {

            @Override
            public void configOkHttp(Context context, OkHttpClient.Builder builder) {

            }
        };
    }

    /**
     * 提供一个 RxCache 配置接口，用于对 RxCache 进行格外的参数配置
     */
    public interface RxCacheConfiguration {

        RxCache configRxCache(Context context, RxCache.Builder builder);

        RxCacheConfiguration EMPTY = new RxCacheConfiguration() {

            @Override
            public RxCache configRxCache(Context context, RxCache.Builder builder) {
                return null;
            }
        };
    }
}