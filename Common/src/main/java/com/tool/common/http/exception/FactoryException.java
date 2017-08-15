package com.tool.common.http.exception;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

/**
 * 异常处理工厂
 */
public class FactoryException {

    // HTTP异常
    private static final String HTTP_ERROR = "状态码异常非[200..300)";
    // 网络连接异常
    private static final String NETWORD_ERROR = "网络连接异常";
    // Json解析异常
    private static final String JSON_ERROR = "数据解析失败";
    // 无法解析该域名
    private static final String UNKOWNHOST_ERROR = "无法解析该域名";
    // 未知异常
    private static final String UNKNOWN_ERROR = "未知异常";

    /**
     * 解析异常
     *
     * @param code
     * @return
     */
    public static ApiException resolveExcetpion(int code) {
        return resolveExcetpion(code, null);
    }

    /**
     * 解析异常
     *
     * @param throwable
     * @return
     */
    public static ApiException resolveExcetpion(Throwable throwable) {
        return resolveExcetpion(-1, throwable);
    }

    /**
     * 解析异常
     *
     * @param code
     * @param throwable
     * @return
     */
    public static ApiException resolveExcetpion(int code, Throwable throwable) {
        if (code != -1) {// HTTP异常
            ApiException apiException = new ApiException();
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setMessage(code + HTTP_ERROR);
            return apiException;
        } else if (throwable != null) {
            ApiException apiException = new ApiException(throwable);
            if (throwable instanceof ConnectException || throwable instanceof SocketTimeoutException) {// 网络连接异常
                apiException.setCode(CodeException.NETWORD_ERROR);
                apiException.setMessage(NETWORD_ERROR);
            } else if (throwable instanceof JSONException || throwable instanceof ParseException) {// Json解析异常
                apiException.setCode(CodeException.JSON_ERROR);
                apiException.setMessage(JSON_ERROR);
            } else if (throwable instanceof UnknownHostException) {// 无法解析该域名异常
                apiException.setCode(CodeException.UNKOWNHOST_ERROR);
                apiException.setMessage(UNKOWNHOST_ERROR);
            } else {// 未知异常
                apiException.setCode(CodeException.UNKNOWN_ERROR);
                apiException.setMessage(throwable.getMessage());
            }
            return apiException;
        } else {// 未知异常
            ApiException apiException = new ApiException();
            apiException.setCode(CodeException.UNKNOWN_ERROR);
            apiException.setMessage(UNKNOWN_ERROR);
            return apiException;
        }
    }
}