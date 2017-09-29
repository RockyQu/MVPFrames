package com.tool.common.http.download;

import android.content.Context;

import com.tool.common.http.download.core.DownloadCore;
import com.tool.common.http.download.helper.DownloaderHelper;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.response.DownloadResponse;
import com.tool.common.http.download.config.DownloaderConfiguration;
import com.tool.common.http.download.exception.DownloadException;

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

    private Context application;

    private DownloaderConfiguration configuration;

    public Downloader() {

    }

    public void init(Context application) {
        if (application == null) {
            throw new IllegalArgumentException("Context must not be null!");
        }

        this.application = application;
        this.configuration = getConfiguration();
    }

    public void init(Context application, DownloaderConfiguration configuration) {
        if (application == null) {
            throw new IllegalArgumentException("Context must not be null!");
        }
        if (configuration == null) {
            throw new IllegalArgumentException("DownloaderConfiguration must not be null!");
        }

        this.application = application;
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
            configuration = DownloaderConfiguration.builder()
                    .application(application)
                    .build();
        }
        return this.configuration;
    }

    public void start(DownloadRequest request, DownloadResponse response) {
        if (application == null) {
            throw new IllegalArgumentException("Not initialized Downloader.getInstance().init(application)");
        }

        DownloaderHelper.init(application, request, response).execute();
    }
}