package com.tool.common.http.download;

import com.tool.common.http.download.exception.DownloadException;

public interface DownloadListener {

    /**
     * 正在连接
     */
    void onConnection();

    /**
     * 开始下载
     */
    void onStart(long total);

    /**
     * 下载进度
     */
    void onProgress(long progress, long total);

    /**
     * 下载失败
     */
    void onFailure(DownloadException exception);

    /**
     * 下载成功
     */
    void onSuccess();

    /**
     * 暂停下载
     */
    void onPaused();

    /**
     * 用户主动取消下载
     */
    void onCanceled();

    /**
     * 完成
     */
    void onFinish();
}