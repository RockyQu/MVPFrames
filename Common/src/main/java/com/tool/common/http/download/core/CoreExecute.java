package com.tool.common.http.download.core;

import com.tool.common.http.download.request.DownloadRequest;

public interface CoreExecute {

    /**
     * 执行下载
     * 返回下载任务的ID{@link DownloadRequest#getId()}，ID默认为下载 Url 的 MD5 值
     *
     * @return 下载任务的ID
     */
    String execute();
}