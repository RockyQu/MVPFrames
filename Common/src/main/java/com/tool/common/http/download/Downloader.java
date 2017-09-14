package com.tool.common.http.download;

import android.content.Context;

/**
 * 下载器、初始化接口
 */
public interface Downloader {

    /**
     * @param context
     */
    void init(Context context);

    /**
     * @param context
     * @param configuration
     */
    void init(Context context, DownloaderConfiguration configuration);
}