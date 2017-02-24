package com.tool.common.di.module;

import com.tool.common.http.converter.GsonConverterBodyFactory;
import com.tool.common.http.cookie.CookieManager;
import com.tool.common.http.interceptor.NetworkInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 管理Http各种参数配置
 */
public class HttpModule {

    private volatile static HttpModule httpModule;

    // 连接、读取、写入超时时间
    private static final int TIME_OUT = 60;
    // 缓存文件最大值100MB
    private static final int CACHE_MAX_SIZE = 1024 * 1024 * 10 * 100;

    // Buidler
    private Buidler buidler;

    // Retrofit
    public Retrofit retrofit;

    public HttpModule(Buidler buidler) {
        this.buidler = buidler;

        OkHttpClient client = configureClient(new OkHttpClient.Builder(), buidler.interceptor);
        retrofit = configureRetrofit(new Retrofit.Builder(), client, buidler.url);
    }

    public static HttpModule getInstance(Buidler buidler) {
        if (httpModule == null) {
            synchronized (HttpModule.class) {
                if (httpModule == null) {
                    httpModule = new HttpModule(buidler);
                }
            }
        }
        return httpModule;
    }

    /**
     * 配置Retrofit
     *
     * @param builder
     * @param client
     * @param baseUrl
     * @return Retrofit
     */
    private Retrofit configureRetrofit(Retrofit.Builder builder, OkHttpClient client, String baseUrl) {
        return builder
                .baseUrl(baseUrl)// 域名
                .client(client)// 设置OkHttpClient
                .addConverterFactory(GsonConverterBodyFactory.create())// 使用Gson
                .build();
    }

    /**
     * 配置OkHttpClient
     *
     * @param okHttpClient
     * @param interceptor
     * @return OkHttpClient
     */
    private OkHttpClient configureClient(OkHttpClient.Builder okHttpClient, Interceptor interceptor) {
        OkHttpClient.Builder builder = okHttpClient
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)// 设置出现错误进行重新连接
                .cache(new Cache(buidler.cacheFile, CACHE_MAX_SIZE))//设置缓存路径和大小
                .addNetworkInterceptor(interceptor)// 网络拦截器，在Request和Resposne是分别被调用一次
                .cookieJar(new CookieManager());// Cookie
        if (buidler.interceptors != null && buidler.interceptors.length > 0) {// Interceptors，只在Response被调用一次
            for (Interceptor item : buidler.interceptors) {
                builder.addInterceptor(item);
            }
        }
        return builder.build();
    }

    /**
     * Retrofit配置，采用Buidler模式
     */
    public static class Buidler {

        // BaseUrl
        private String url;
        // 缓存目录
        private File cacheFile;
        // 网络拦截器
        private NetworkInterceptor interceptor;
        // 拦截器
        private Interceptor[] interceptors;

        private Buidler() {
            ;
        }

        public static Buidler buidler() {
            return new Buidler();
        }

        public Buidler url(String url) {
            this.url = url;
            return this;
        }

        public Buidler cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Buidler networkInterceptor(NetworkInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public Buidler interceptors(Interceptor[] interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public HttpModule build() {
            return HttpModule.getInstance(this);
        }
    }
}