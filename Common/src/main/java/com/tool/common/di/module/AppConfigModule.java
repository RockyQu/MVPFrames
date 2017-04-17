package com.tool.common.di.module;

import android.app.Application;
import android.text.TextUtils;

import com.tool.common.http.NetworkHandler;
import com.tool.common.utils.FileUtils;

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

    public AppConfigModule(Builder buidler) {
        this.httpUrl = buidler.httpUrl;
        this.cacheFile = buidler.cacheFile;
        this.networkHandler = buidler.networkHandler;
        this.interceptors = buidler.interceptors;
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

        public AppConfigModule build() {
            return new AppConfigModule(this);
        }
    }
}