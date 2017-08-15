package com.tool.common.http.exception;

/**
 * 统一异常处理
 */
public class ApiException extends Exception{

    // 错误码
    private int code;
    // 显示的信息
    private String message;

    public ApiException(Throwable e) {
        super(e);
    }

    public ApiException(Throwable cause,@CodeException.CodeEp int code, String message) {
        super(message, cause);

        this.code = code;
        this.message = message;
    }

    @CodeException.CodeEp
    public int getCode() {
        return code;
    }

    public void setCode(@CodeException.CodeEp int code) {
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