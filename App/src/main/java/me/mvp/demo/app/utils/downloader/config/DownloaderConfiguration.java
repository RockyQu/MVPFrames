package me.mvp.demo.app.utils.downloader.config;

import android.app.Application;
import android.text.TextUtils;

import me.mvp.demo.app.utils.downloader.Downloader;
import me.mvp.demo.app.utils.downloader.Tasker;

/**
 * 相关参数配置
 * <p>
 * 该类配合{@link Downloader#init(DownloaderConfiguration)}方法进行使用
 * 建议在{@link Application#onCreate()}方法调用初始化配置代码
 */
public class DownloaderConfiguration {

    private Application application;
    private String defaultSaveRootPath;

    private boolean debug;

    public DownloaderConfiguration(Builder builder) {
        this.application = builder.application;
        this.defaultSaveRootPath = builder.defaultSaveRootPath;
        this.debug = builder.debug;
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

    public boolean isDebug() {
        return debug;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Application application;

        /**
         * 默认保存路径，所有下载的文件会保存至这个目录，如果在单独的下载任务{@link Tasker#setSaveFilePath(String)}设置了单独的保存路径，则会覆盖此路径
         */
        private String defaultSaveRootPath;

        // 调试模式
        private boolean debug = false;

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

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public DownloaderConfiguration build() {
            if (application == null) {
                throw new NullPointerException();
            }
            return new DownloaderConfiguration(this);
        }
    }
}