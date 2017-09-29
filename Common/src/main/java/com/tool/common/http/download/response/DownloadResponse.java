package com.tool.common.http.download.response;

import com.tool.common.http.download.exception.DownloadException;

public interface DownloadResponse {

    void onStart();

    void onProgress(int progress, long total);

    void onFailure();

    void onFinish(DownloadException exception);
}