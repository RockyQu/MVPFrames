package com.tool.common.http;

import com.google.gson.annotations.SerializedName;

/**
 * Response Result
 */
public class ResponseEntity<T> {

    // 是否登录
    @SerializedName("login")
    private boolean login;
    // 提示信息
    @SerializedName(value = "message", alternate = {"msg"})
    private String message;
    // 请求是否成功
    @SerializedName("success")
    private boolean success;
    // 系统是否正常
    @SerializedName(value = "system", alternate = {"sys"})
    private boolean system;
    // 其他状态
    @SerializedName("code")
    private int code;
    // 数量
    @SerializedName(value = "count", alternate = {"totalProperty"})
    private long count;
    // 请求结果
    @SerializedName(value = "data", alternate = {"result"})
    private T data;

    public ResponseEntity() {
        ;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}