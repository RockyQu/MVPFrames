package com.tool.common.http.download.helper;


import android.content.Context;

import com.tool.common.http.download.core.CoreExecute;
import com.tool.common.http.download.core.DownloadCore;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.request.IdGenerator;
import com.tool.common.http.download.request.TaskIdGenerator;
import com.tool.common.http.download.response.DownloadResponse;

public class DownloaderHelper {

    public static IdGenerator getTaskIdGenerator() {
        return TaskIdGenerator.getInstance();
    }

    public static CoreExecute init(Context application, DownloadRequest request, DownloadResponse response) {
        return new DownloadCore(application, request, response);
    }
}