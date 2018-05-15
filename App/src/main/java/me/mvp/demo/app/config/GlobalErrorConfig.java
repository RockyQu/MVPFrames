package me.mvp.demo.app.config;

import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.di.module.AppModule;
import me.mvp.frame.widget.Toaster;
import retrofit2.HttpException;

/**
 * 全局异常回调，可以不限于只处理 HTTP 异常，其他模块也可以回调此接口来处理统一异常管理
 * 通过 {@link AppComponent#getGlobalErrorHandler()} 获取此接口
 */
public class GlobalErrorConfig implements AppModule.GlobalErrorHandler {

    @Override
    public void httpError(Context context, Throwable throwable) {
        // 可以根据不同的错误做不同的逻辑处理
        String message = "未知错误";
        if (throwable instanceof UnknownHostException) {
            message = "网络不可用或无法解析该域名";
        } else if (throwable instanceof ConnectException || throwable instanceof SocketTimeoutException) {
            message = "请求网络超时或网络连接异常";
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            if (httpException.code() == 500) {
                message = "服务器发生错误";
            } else if (httpException.code() == 404) {
                message = "请求地址不存在";
            } else if (httpException.code() == 403) {
                message = "请求被服务器拒绝";
            } else if (httpException.code() == 307) {
                message = "请求被重定向到其他页面";
            } else {
                message = httpException.message();
            }
        } else if (throwable instanceof JsonParseException || throwable instanceof ParseException || throwable instanceof JSONException) {
            message = "数据解析错误";
        }

        Toaster.postMessage(message);
    }

    @Override
    public void handlerError(Context context, Throwable throwable) {

    }
}