package com.tool.common.http.download.helper;


import com.tool.common.http.download.request.IdGenerator;
import com.tool.common.http.download.request.TaskIdGenerator;

public class DownloaderHelper {

    public static IdGenerator getTaskIdGenerator() {
        return TaskIdGenerator.getInstance();
    }
}