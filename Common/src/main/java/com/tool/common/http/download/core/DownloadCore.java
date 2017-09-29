package com.tool.common.http.download.core;

import com.tool.common.http.download.exception.DownloadException;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.response.DownloadResponse;

public class DownloadCore {

    private DownloadRequest request;
    private DownloadResponse response;

    public DownloadCore(DownloadRequest request, DownloadResponse response) {
        this.request = request;
        this.response = response;
    }

    private void executen() throws DownloadException {

    }
}