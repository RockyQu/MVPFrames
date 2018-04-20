package me.mvp.frame.http.download;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import me.mvp.frame.http.download.exception.DownloadException;

public abstract class DownloaderSampleListener extends FileDownloadListener {

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

    }

    @Override
    protected void warn(BaseDownloadTask task) {

    }

    /**
     * 已经连接到服务器，并收到Http响应。
     */
    @Override
    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        this.onConnection(isContinue, soFarBytes, totalBytes);
    }

    /**
     * 从网络获取数据并写入本地磁盘。
     */
    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        this.onProgress(soFarBytes, totalBytes, task.getSpeed());
    }

    /**
     * 暂停任务，当调用{@link Tasker#pause()} 时回调此方法。
     */
    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        this.onPaused();
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        this.onFailure(new DownloadException(e));
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        this.onComplete(task.getTargetFilePath());
    }

    /**
     * 正在连接
     *
     * @param isContinue 是否从断点恢复
     * @param progress   当前进度
     * @param total      文件大小
     */
    protected abstract void onConnection(boolean isContinue, long progress, long total);

    /**
     * 下载进度
     *
     * @param progress 当前进度
     * @param total    文件大小
     * @param speed    下载速度
     */
    protected abstract void onProgress(long progress, long total, int speed);

    /**
     * 下载失败
     *
     * @param exception 异常信息
     */
    protected abstract void onFailure(DownloadException exception);

    /**
     * 暂停下载
     */
    protected abstract void onPaused();

    /**
     * 下载完成
     */
    protected abstract void onComplete(String filePath);
}