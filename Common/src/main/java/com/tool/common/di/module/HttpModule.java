package com.tool.common.di.module;

import android.app.Application;

import com.tool.common.http.converter.GsonConverterBodyFactory;
import com.tool.common.http.converter.JsonConverterFactory;
import com.tool.common.http.cookie.CookieManager;
import com.tool.common.http.interceptor.NetworkInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * HTTP参数配置
 */
@Module
public class HttpModule {

    private Application application;

    // 连接、读取、写入超时时间
    private static final int TIME_OUT = 60;
    // 缓存文件最大值100MB
    private static final int CACHE_MAX_SIZE = 1024 * 1024 * 10 * 100;

    public HttpModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl) {
        return builder
                .baseUrl(httpUrl)// 域名
                .client(client)// 设置OkHttpClient
                .addConverterFactory(GsonConverterBodyFactory.create())// 支持Gson
                .addConverterFactory(JsonConverterFactory.create())// 支持Json
                .build();
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder okHttpClient, Interceptor interceptor, List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = okHttpClient
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)// 设置出现错误进行重新连接
                .cache(new Cache(application.getCacheDir(), CACHE_MAX_SIZE))// 设置缓存路径和大小
                .addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor)// 网络拦截器，在Request和Resposne是分别被调用一次
                .cookieJar(new CookieManager());// Cookie
        if (interceptors != null && interceptors.size() > 0) {// Interceptors，只在Response被调用一次
            for (Interceptor item : interceptors) {
                builder.addInterceptor(item);
            }
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
     * Http拦截器
     */
    @Singleton
    @Provides
    Interceptor provideNetworkInterceptor(NetworkInterceptor interceptor) {
        return interceptor;
    }
}