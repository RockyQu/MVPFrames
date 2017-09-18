package com.tool.common.http.download.task;

import com.tool.common.http.download.config.DownloaderConfiguration;

public class DownloadTask {

    // ID
    private String id;

    /**
     * Url
     */
    private String url;

    /**
     * 保存路径
     * <p>
     * 设置这个变量会覆盖{@link DownloaderConfiguration#defaultSaveRootPath}，只对当前Task有效
     */
    private String saveFilePath;

    /**
     * 文件名称
     * <p>
     * 自定义保存文件名称
     */
    private String saveFileName;

    public DownloadTask() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }
}