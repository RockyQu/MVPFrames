package com.tool.common.http.download.config;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.logg.Logg;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.Downloader;
import com.tool.common.utils.ProjectUtils;

/**
 * 相关参数配置
 * <p>
 * 该类配合{@link Downloader#init(DownloaderConfiguration)}方法进行使用
 * 建议在{@link Application#onCreate()}方法调用初始化配置代码
 */
public class DownloaderConfiguration {

    private Application application;
    private String defaultSaveRootPath;

    public DownloaderConfiguration(Builder builder) {
        this.application = builder.application;
        this.defaultSaveRootPath = builder.defaultSaveRootPath;
    }

    public Application getApplication() {
        return application;
    }

    public String getDefaultSaveRootPath() {
        if (TextUtils.isEmpty(defaultSaveRootPath)) {
            return application.getCacheDir().toString();
        }
        return defaultSaveRootPath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Application application;

        /**
         * 默认保存路径，所有下载的文件会保存至这个目录，如果在单独的下载任务{@link DownloadRequest#saveFilePath}设置了单独的保存路径，则会覆盖此路径
         */
        private String defaultSaveRootPath;

        private Builder() {
            ;
        }

        public Builder application(Application application) {
            this.application = application;
            return this;
        }

        public Builder defaultSaveRootPath(String defaultSaveRootPath) {
            this.defaultSaveRootPath = defaultSaveRootPath;
            return this;
        }

        public DownloaderConfiguration build() {
            if (application == null) {
                throw new IllegalArgumentException("Application must not be null!");
            }
            return new DownloaderConfiguration(this);
        }
    }
}