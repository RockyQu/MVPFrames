package com.tool.common.http.exception;

/**
 * 统一异常处理
 */
public class ApiException extends Exception {

    // 错误码
    private int code;
    // 显示的信息
    private String message;

    public ApiException() {
        super();
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(Throwable throwable, int code, String message) {
        super(message, throwable);

        this.code = code;
        this.message = message;
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