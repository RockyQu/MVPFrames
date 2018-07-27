package me.mvp.demo.entity.base;

import com.google.gson.annotations.SerializedName;

import me.mvp.demo.app.api.Api;

/**
 * 如果你后台的服务器返回的 Json 格式类似于这种，可以使用此类
 * 指定范型来改变 {@code data} 字段的类型, 达到重用 {@link ResponseEntity}
 */
public class ResponseEntity<T> {

    // 状态
    @SerializedName("code")
    private int code;
    // 提示信息
    @SerializedName(value = "message", alternate = {"msg"})
    private String message;
    // 请求结果
    @SerializedName(value = "data", alternate = {"result"})
    private T data;

    public ResponseEntity() {
        ;
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

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (code == Api.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }
}