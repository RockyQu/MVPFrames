package com.tool.common.http.download;

import com.tool.common.http.download.helper.DownloaderHelper;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.config.DownloaderConfiguration;

/**
 * 下载管理
 * <p>
 * 这是一个轻量级下载器，作为日常简单下载功能使用，如需要其他复杂功能，请集成其他下载框架
 * <p>
 * 该模块特点
 * 1、不支持多任务，队列下载
 * 2、不支持断点继传
 * 3、不支持暂停正在下载的任务
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