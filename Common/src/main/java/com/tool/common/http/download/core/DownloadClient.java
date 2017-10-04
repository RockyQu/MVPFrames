package com.tool.common.http.download.core;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class DownloadClient {

    // 连接、读取、写入超时时间
    private static final int TIME_OUT = 60;

    private OkHttpClient okHttpClient = null;

    public DownloadClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);// 设置出现错误进行重新连接
        okHttpClient = builder.build();
    }

    private final static class HolderClass {
        private final static DownloadClient INSTANCE = new DownloadClient();
    }

    public static DownloadClient getInstance() {
        return HolderClass.INSTANCE;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}