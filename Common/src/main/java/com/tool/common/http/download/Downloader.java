package com.tool.common.http.download;

import android.app.Application;

import com.tool.common.http.download.helper.DownloaderHelper;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.config.DownloaderConfiguration;

/**
 * 下载管理
 * <p>
 * 这是一个轻量级下载器，作为日常简单下载功能使用
 * 请在{@link Application#onCreate()}方法调用初始化配置代码
 */
public class Downloader {

    private DownloaderConfiguration configuration;

    private Downloader() {

    }

    public void init(DownloaderConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("DownloaderConfiguration must not be null!");
        }

        this.configuration = configuration;
    }

    private final static class HolderClass {
        private final static Downloader INSTANCE = new Downloader();
    }

    public static Downloader getInstance() {
        return HolderClass.INSTANCE;
    }

    public DownloaderConfiguration getConfiguration() {
        return configuration;
    }

    public String start(DownloadRequest request, DownloadListener listener) {
        if (request == null) {
            throw new IllegalArgumentException("DownloadRequest must not be null!");
        }

        return DownloaderHelper.init(configuration.getApplication(), request, listener).execute();
    }
}