package com.tool.common.http.download.request;

import com.tool.common.http.download.helper.DownloaderUtils;

public class TaskIdGenerator implements IdGenerator {

    private final static class HolderClass {
        private final static TaskIdGenerator INSTANCE = new TaskIdGenerator();
    }

    public static TaskIdGenerator getInstance() {
        return HolderClass.INSTANCE;
    }

    @Override
    public String generateId(String url) {
        return DownloaderUtils.md5(DownloaderUtils.formatString("%s", url));
    }
}