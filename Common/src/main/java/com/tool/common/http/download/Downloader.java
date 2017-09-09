package com.tool.common.http.download;

import android.content.Context;

/**
 * 下载器对用户的开放接口
 */
public interface Downloader {

    void init(Context context);

    void init(Context context, DownloaderConfiguration configuration);
}