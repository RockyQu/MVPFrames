package com.tool.common.http;

import com.google.gson.annotations.SerializedName;

/**
 * Response Result
 */
public class ResponseEntity<T> {

    @SerializedName("code")
    private int code;
    @SerializedName("info")
    private String message;
    @SerializedName("result")
    private T data;

    public ResponseEntity(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}