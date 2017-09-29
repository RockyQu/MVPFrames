package com.tool.common.http.download.response;

import com.tool.common.http.download.exception.DownloadException;
import com.tool.common.http.download.request.DownloadRequest;

public interface DownloadResponse {

    void onStart(long total);

    void onProgress(long progress, long total);

    void onFailure(DownloadException exception);

    void onFinish(DownloadRequest request);
}