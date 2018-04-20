package me.mvp.frame.http.download.exception;

public class DownloadException extends Exception {

    private int code;
    private String exceptionMessage;

    public DownloadException() {

    }

    public DownloadException(String exceptionMessage) {
        super(exceptionMessage);
        this.exceptionMessage = exceptionMessage;
    }

    public DownloadException(int code, String exceptionMessage) {
        super(exceptionMessage);
        this.code = code;
        this.exceptionMessage = exceptionMessage;
    }

    public DownloadException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public DownloadException(String exceptionMessage, Throwable throwable) {
        super(exceptionMessage, throwable);
        this.exceptionMessage = exceptionMessage;
    }

    public DownloadException(int code, String exceptionMessage, Throwable throwable) {
        super(exceptionMessage, throwable);
        this.code = code;
        this.exceptionMessage = exceptionMessage;
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

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}