package com.tool.common.http.download;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
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
            throw new NullPointerException();
        }

        this.configuration = configuration;

        FileDownloadLog.NEED_LOG = configuration.isDebug();
        FileDownloader.setup(configuration.getApplication());
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

    public Tasker create(String url) {
        return new Tasker(url);
    }
}