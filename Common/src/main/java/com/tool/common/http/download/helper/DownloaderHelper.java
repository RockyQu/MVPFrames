package com.tool.common.http.download.helper;

import android.app.Application;

import com.tool.common.http.download.core.CoreExecute;
import com.tool.common.http.download.core.DownloadCore;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.request.IdGenerator;
import com.tool.common.http.download.request.TaskIdGenerator;
import com.tool.common.http.download.DownloadListener;

public class DownloaderHelper {

    public static IdGenerator getTaskIdGenerator() {
        return TaskIdGenerator.getInstance();
    }

    public static CoreExecute init(Application application, DownloadRequest request, DownloadListener listener) {
        return new DownloadCore(application, request, listener);
    }
}