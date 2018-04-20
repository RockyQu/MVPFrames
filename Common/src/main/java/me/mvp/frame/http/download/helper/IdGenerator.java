package me.mvp.frame.http.download.helper;

public interface IdGenerator {

    /**
     * 根据下载地址生成一个唯一的任务ID
     *
     * @param url download url
     * @return download task id.
     */
    String generateId(String url);
}