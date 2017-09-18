package com.tool.common.http.download.config;

import android.app.Application;
import android.content.Context;

import com.tool.common.http.download.task.DownloadTask;
import com.tool.common.http.download.Downloader;
import com.tool.common.utils.ProjectUtils;

/**
 * 相关参数配置
 * <p>
 * 该类配合{@link Downloader#init(Context, DownloaderConfiguration)}方法进行使用
 * {@link Downloader#init(Context, DownloaderConfiguration)}方法为非必须调用，根据实际需求进行配置
 * 建议在{@link Application#onCreate()}方法调用初始化配置代码
 */
public class DownloaderConfiguration {

    private String defaultSaveRootPath;

    public DownloaderConfiguration(Builder builder) {
        this.defaultSaveRootPath = builder.defaultSaveRootPath;
    }

    public String getDefaultSaveRootPath() {
        return defaultSaveRootPath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        /**
         * 默认保存路径，所有下载的文件会保存至这个目录，如果在单独的下载任务{@link DownloadTask#saveFilePath}设置了单独的保存路径，则会覆盖此路径
         */
        private String defaultSaveRootPath = ProjectUtils.OTHER;

        private Builder() {
            ;
        }

        public Builder defaultSaveRootPath(String defaultSaveRootPath) {
            this.defaultSaveRootPath = defaultSaveRootPath;
            return this;
        }

        public DownloaderConfiguration build() {
            return new DownloaderConfiguration(this);
        }
    }
}