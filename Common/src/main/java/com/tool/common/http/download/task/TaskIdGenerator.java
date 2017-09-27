package com.tool.common.http.download.task;

import com.tool.common.http.download.utils.DownloaderUtils;

public class TaskIdGenerator implements IdGenerator {

    @Override
    public int generateId(String url) {
        return DownloaderUtils.md5(DownloaderUtils.formatString("%sp%s@dir", url)).hashCode();
    }
}