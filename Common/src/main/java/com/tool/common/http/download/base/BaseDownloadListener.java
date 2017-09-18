package com.tool.common.http.download.base;

/**
 *
 */
public interface BaseDownloadListener {

    /**
     *
     */
    void onStart();

    /**
     *
     */
    void onFailure();

    /**
     *
     */
    void onFinish();
}