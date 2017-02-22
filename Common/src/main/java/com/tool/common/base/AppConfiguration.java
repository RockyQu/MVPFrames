package com.tool.common.base;

import android.text.TextUtils;

import com.tool.common.http.interceptor.NetworkInterceptor;
import com.tool.common.widget.imageloader.ImageLoader;

import java.io.File;

import okhttp3.Interceptor;

/**
 * App全局配置信息
 */
public class AppConfiguration {

    private volatile static AppConfiguration appConfiguration;

    // 调试模式
    private boolean debug;

    // Http通信Base接口
    private String httpUrl;
    // Http缓存路径
    private File httpCacheFile;
    // 网络通信拦截器
    private NetworkInterceptor networkInterceptor;
    // 其他拦截器
    private Interceptor[] interceptors;

    public AppConfiguration(Buidler buidler) {
        this.debug = buidler.debug;
        this.httpUrl = buidler.httpUrl;
        this.httpCacheFile = buidler.httpCacheFile;
        this.networkInterceptor = buidler.networkInterceptor;
        this.interceptors = buidler.interceptors;
    }

    public static AppConfiguration getInstance(Buidler buidler) {
        if (appConfiguration == null) {
            synchronized (AppConfiguration.class) {
                if (appConfiguration == null) {
                    appConfiguration = new AppConfiguration(buidler);
                }
            }
        }
        return appConfiguration;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public File getHttpCacheFile() {
        return httpCacheFile;
    }

    public NetworkInterceptor getNetworkInterceptor() {
        return networkInterceptor;
    }

    public Interceptor[] getInterceptors() {
        return interceptors;
    }

    /**
     * App全局配置，采用Buidler模式
     */
    public static class Buidler {

        // 调试模式
        private boolean debug = false;

        // Http通信Base接口
        private String httpUrl;
        // Http缓存路径
        private File httpCacheFile;
        // 网络通信拦截器
        private NetworkInterceptor networkInterceptor;
        // 其他拦截器
        private Interceptor[] interceptors;

        private Buidler() {
            ;
        }

        public static Buidler buidler() {
            return new Buidler();
        }

        public Buidler debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Buidler httpUrl(String httpUrl) {
            if (TextUtils.isEmpty(httpUrl)) {
                throw new IllegalArgumentException("Http url can not be empty!");
            }
            this.httpUrl = httpUrl;
            return this;
        }

        public Buidler httpCacheFile(File httpCacheFile) {
            if (httpCacheFile == null) {
                throw new IllegalArgumentException("Http CacheFile can not be empty!");
            }
            this.httpCacheFile = httpCacheFile;
            return this;
        }

        public Buidler networkInterceptor(NetworkInterceptor networkInterceptor) {
            if (networkInterceptor == null) {
                throw new IllegalArgumentException("NetworkInterceptor can not be empty!");
            }
            this.networkInterceptor = networkInterceptor;
            return this;
        }

        public Buidler interceptors(Interceptor[] interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public AppConfiguration build() {
            return AppConfiguration.getInstance(this);
        }
    }
}