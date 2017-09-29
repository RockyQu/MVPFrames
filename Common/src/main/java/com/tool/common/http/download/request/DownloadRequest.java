package com.tool.common.http.download.request;

import android.text.TextUtils;

import com.tool.common.http.download.Downloader;
import com.tool.common.http.download.config.DownloaderConfiguration;
import com.tool.common.http.download.helper.DownloaderHelper;

public class DownloadRequest {

    /**
     * 任务Id
     * <p>
     * 默认为对 Url 进行 MD5 加密后生成的值
     */
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
     * <p>
     * 默认为对 Url 进行 MD5 加密后生成的值
     */
    private CharSequence saveFileName;

    public DownloadRequest() {

    }

    public DownloadRequest(String url) {
        this.url = url;
    }

    public DownloadRequest(String url, String saveFilePath) {
        this.url = url;
        this.saveFilePath = saveFilePath;
    }

    public DownloadRequest(String url, String saveFileName, String saveFilePath) {
        this.url = url;
        this.saveFileName = saveFileName;
        this.saveFilePath = saveFilePath;
    }

    public String getId() {
        if (TextUtils.isEmpty(getUrl())) {
            throw new NullPointerException("Url is required");
        }

        if (TextUtils.isEmpty(id)) {
            return DownloaderHelper.getTaskIdGenerator().generateId(getUrl());
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("Url is required");
        }
        this.url = url;
    }

    public String getSaveFilePath() {
        if (!TextUtils.isEmpty(saveFilePath)) {
            return saveFilePath;
        } else {
            return Downloader.getInstance().getConfiguration().getDefaultSaveRootPath();
        }
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public CharSequence getSaveFileName() {
        if (TextUtils.isEmpty(saveFileName)) {
            return getId();
        }
        return saveFileName;
    }

    public void setSaveFileName(CharSequence saveFileName) {
        this.saveFileName = saveFileName;
    }
}