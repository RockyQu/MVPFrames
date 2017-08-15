package com.tool.common.http.exception;


import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 异常处理工厂
 * 主要是解析异常，输出自定义ApiException
 * Created by WZG on 2016/12/12.
 */
public class FactoryException {

    private static final String HttpException_MSG = "网络错误";
    private static final String ConnectException_MSG = "连接失败";
    private static final String JSONException_MSG = "fastjeson解析失败";
    private static final String UnknownHostException_MSG = "无法解析该域名";

    /**
     * 解析异常
     *
     * @param e
     * @return
     */
    public static ApiException analysisExcetpion(Throwable e) {
        ApiException apiException = new ApiException(e);
        if (e instanceof HttpException) {
             /*网络异常*/
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setMessage(HttpException_MSG);
        }else if (e instanceof ConnectException||e instanceof SocketTimeoutException) {
             /*链接异常*/
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setMessage(ConnectException_MSG);
        } else if ( e instanceof JSONException || e instanceof ParseException) {
            apiException.setCode(CodeException.JSON_ERROR);
            apiException.setMessage(JSONException_MSG);
        }else if (e instanceof UnknownHostException){
            /*无法解析该域名异常*/
            apiException.setCode(CodeException.UNKOWNHOST_ERROR);
            apiException.setMessage(UnknownHostException_MSG);
        } else {
            /*未知异常*/
            apiException.setCode(CodeException.UNKNOWN_ERROR);
            apiException.setMessage(e.getMessage());
        }
        return apiException;
    }
}