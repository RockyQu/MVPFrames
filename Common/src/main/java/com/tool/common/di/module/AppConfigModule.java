package com.tool.common.di.module;

import android.app.Application;
import android.text.TextUtils;

import com.tool.common.http.NetworkHandler;
import com.tool.common.utils.FileUtils;
import com.tool.common.widget.imageloader.BaseImageLoader;
import com.tool.common.widget.imageloader.glide.GlideImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * App全局配置信息
 */
@Module
public class AppConfigModule {

    private HttpUrl httpUrl;
    private File cacheFile;
    private NetworkHandler networkHandler;
    private List<Interceptor> interceptors;

    private BaseImageLoader imageLoader;

    private HttpModule.RetrofitConfiguration retrofitConfiguration;
    private HttpModule.OkHttpConfiguration okHttpConfiguration;

    private AppModule.GsonConfiguration gsonConfiguration;

    public AppConfigModule(Builder buidler) {
        this.httpUrl = buidler.httpUrl;
        this.cacheFile = buidler.cacheFile;
        this.networkHandler = buidler.networkHandler;
        this.interceptors = buidler.interceptors;

        this.imageLoader = buidler.imageLoader;

        this.retrofitConfiguration = buidler.retrofitConfiguration;
        this.okHttpConfiguration = buidler.okHttpConfiguration;

        this.gsonConfiguration = buidler.gsonConfiguration;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        return httpUrl;
    }

    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return cacheFile == null ? FileUtils.getCacheFile(application) : cacheFile;
    }

    @Singleton
    @Provides
    NetworkHandler provideNetworkHandler() {
        return networkHandler == null ? NetworkHandler.EMPTY : networkHandler;
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
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return gsonConfiguration == null ? AppModule.GsonConfiguration.EMPTY : gsonConfiguration;
    }

    /**
     * App全局配置
     */
    public static class Builder {

        // Http通信Base接口
        private HttpUrl httpUrl;
        // Http缓存路径
        private File cacheFile;
        // 网络通信拦截器
        private NetworkHandler networkHandler;
        // 其他拦截器
        private List<Interceptor> interceptors = new ArrayList<>();

        // 图片框架，默认为Glide
        private BaseImageLoader imageLoader;

        // 提供一个Retrofit配置接口，用于对Retrofit进行格外的参数配置
        private HttpModule.RetrofitConfiguration retrofitConfiguration;
        // 提供一个OkHttp配置接口，用于对OkHttp进行格外的参数配置
        private HttpModule.OkHttpConfiguration okHttpConfiguration;

        // 提供一个Gson配置接口，用于对Gson进行格外的参数配置
        private AppModule.GsonConfiguration gsonConfiguration;

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

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder networkHandler(NetworkHandler networkHandler) {
            this.networkHandler = networkHandler;
            return this;
        }

        public Builder interceptors(Interceptor[] interceptors) {
            this.interceptors = Arrays.asList(interceptors);
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

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public AppConfigModule build() {
            return new AppConfigModule(this);
        }
    }
}