package com.tool.common.http.download;

import android.content.Context;

import com.tool.common.http.download.config.DownloaderConfiguration;

/**
 * 下载器管理
 */
public class Downloader {

    private DownloaderConfiguration configuration;

    public Downloader() {


    }

    public void init(Context context) {
        this.init(context, getConfiguration());
    }

    public void init(Context context, DownloaderConfiguration configuration) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null!");
        }
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
        if (this.configuration == null) {
            DownloaderConfiguration.builder()
                    .build();
        }
        return this.configuration;
    }
}