package com.tool.common.http.download.helper;

public class TaskerIdGenerator implements IdGenerator {

    private final static class HolderClass {
        private final static TaskerIdGenerator INSTANCE = new TaskerIdGenerator();
    }

    public static TaskerIdGenerator getInstance() {
        return HolderClass.INSTANCE;
    }

    @Override
    public String generateId(String url) {
        return DownloaderUtils.md5(DownloaderUtils.formatString("%s", url));
    }
}