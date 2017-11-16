package com.tool.common.http.download;

import android.text.TextUtils;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tool.common.http.download.config.DownloaderConfiguration;
import com.tool.common.http.download.helper.TaskerIdGenerator;

import java.io.File;

public class Tasker {

    private String saveFileName;

    private Object tag;

    private int callbackProgressCount = 150;

    private int minIntervalUpdateSpeedMs = 200;

    private FileDownloadListener listener;

    // 一个下载任务
    private BaseDownloadTask downloadTask;

    DownloaderConfiguration configuration;

    public Tasker(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException();
        }

        configuration = Downloader.getInstance().getConfiguration();

        downloadTask = FileDownloader.getImpl().create(url);

        if (!TextUtils.isEmpty(configuration.getDefaultSaveRootPath())) {
            setSaveFilePath(configuration.getDefaultSaveRootPath(), true);
        }
    }

    /**
     * 任务 Id
     */
    public int getId() {
        return downloadTask.getId();
    }

    /**
     * 下载地址
     */
    public String getUrl() {
        return downloadTask.getUrl();
    }

    /**
     * 保存完整的路径
     */
    public String getSaveFilePath() {
        return downloadTask.getTargetFilePath();
    }

    /**
     * 保存路径
     * <p>
     * 设置这个变量会覆盖{@link DownloaderConfiguration#defaultSaveRootPath}，只对当前任务有效
     *
     * @param saveFilePath 完整的保存路径包括文件名
     */
    public Tasker setSaveFilePath(String saveFilePath) {
        return setSaveFilePath(saveFilePath, false);
    }

    /**
     * 保存路径
     * <p>
     * 设置这个变量会覆盖{@link DownloaderConfiguration#defaultSaveRootPath}，只对当前任务有效
     *
     * @param saveFilePath    保存路径不包括文件名
     * @param pathAsDirectory 是否为路径，如果 saveFilePath 传入以 / 结尾的路径 pathAsDirectory 需要设置为 true
     */
    public Tasker setSaveFilePath(String saveFilePath, boolean pathAsDirectory) {
        if (TextUtils.isEmpty(saveFilePath)) {
            throw new NullPointerException();
        }

        if (pathAsDirectory) {
            this.saveFileName = TaskerIdGenerator.getInstance().generateId(downloadTask.getUrl());

            downloadTask.setPath(saveFilePath + getSaveFileName());
        } else {
            this.saveFileName = new File(saveFilePath).getName();

            downloadTask.setPath(saveFilePath);
        }

        return this;
    }

    /**
     * 文件名
     */
    public String getSaveFileName() {
        return saveFileName;
    }

    public Object getTag() {
        return tag;
    }

    /**
     * 设置与此任务相关的标签
     */
    public Tasker setTag(Object tag) {
        this.tag = tag;

        if (tag != null) {
            downloadTask.setTag(tag);
        }
        return this;
    }

    public int getCallbackProgressCount() {
        return callbackProgressCount;
    }

    /**
     * 设置最大回调计数
     */
    public Tasker setCallbackProgressCount(int callbackProgressCount) {
        this.callbackProgressCount = callbackProgressCount;

        if (callbackProgressCount != 0) {
            downloadTask.setCallbackProgressTimes(callbackProgressCount);
        }
        return this;
    }

    public int getMinIntervalUpdateSpeedMs() {
        return minIntervalUpdateSpeedMs;
    }

    /**
     * 更新下载的最小间隔毫秒
     */
    public Tasker setMinIntervalUpdateSpeedMs(int minIntervalUpdateSpeedMs) {
        this.minIntervalUpdateSpeedMs = minIntervalUpdateSpeedMs;

        if (minIntervalUpdateSpeedMs != 0) {
            downloadTask.setMinIntervalUpdateSpeed(minIntervalUpdateSpeedMs);
        }
        return this;
    }

    public FileDownloadListener getListener() {
        return listener;
    }

    /**
     * 状态监听
     */
    public Tasker setListener(FileDownloadListener listener) {
        this.listener = listener;

        downloadTask.setListener(listener);
        return this;
    }

    /**
     * 开始下载
     */
    public int start() {
        return downloadTask.start();
    }

    /**
     * 暂停下载
     */
    public boolean pause() {
        return downloadTask.pause();
    }
}