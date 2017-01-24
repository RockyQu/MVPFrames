package com.tool.common.http.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 接口共通参数
 * 为所有接口以Get方式添加共通参数，如版本号、Token等
 */
public class ParameterInterceptor implements Interceptor {

    private final ParameterCallback callback;

    public ParameterInterceptor() {
        this(ParameterCallback.DEFAULT);
    }

    public ParameterInterceptor(ParameterCallback callback) {
        this.callback = callback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        HttpUrl.Builder modifiedUrl = originalRequest.url().newBuilder()
                .scheme(originalRequest.url().scheme())
                .host(originalRequest.url().host());

        HashMap<String, String> parameters = callback.parameters();
        if (parameters != null && parameters.size() != 0) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                modifiedUrl.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        Request request = originalRequest.newBuilder()
                .method(originalRequest.method(), originalRequest.body())
                .url(modifiedUrl.build())
                .build();
        return chain.proceed(request);
    }

    /**
     * 参数回调接口
     */
    public interface ParameterCallback {

        HashMap<String, String> parameters();

        ParameterCallback DEFAULT = new ParameterCallback() {

            @Override
            public HashMap<String, String> parameters() {
                return null;
            }
        };
    }
}