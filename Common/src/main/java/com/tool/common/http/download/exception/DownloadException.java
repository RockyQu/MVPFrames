package com.tool.common.http.download.exception;

public class DownloadException extends Exception {

    private int code;
    private String message;

    public DownloadException() {

    }

    public DownloadException(String message) {
        super(message);
        this.message = message;
    }

    public DownloadException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public DownloadException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public DownloadException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
    }

    public DownloadException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.message = message;
    }

    public DownloadException(Throwable throwable) {
        super(throwable);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}