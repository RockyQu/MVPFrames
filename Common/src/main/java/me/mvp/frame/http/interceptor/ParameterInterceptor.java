package me.mvp.frame.http.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 接口共通参数
 * 为所有接口以Get、POST方式添加共通参数，如版本号、Token等
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
        Request request = chain.request();

        HashMap<String, Object> parameters = callback.parameters();
        if (parameters != null && parameters.size() != 0) {
            if (request.method().equals("GET")) {// 为GET方式统一添加请求参数
                HttpUrl.Builder modifiedUrl = request.url().newBuilder()
                        .scheme(request.url().scheme())
                        .host(request.url().host());

                if (parameters != null && parameters.size() != 0) {
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        modifiedUrl.addQueryParameter(entry.getKey(), entry.getValue().toString());
                    }
                }

                request = request.newBuilder()
                        .method(request.method(), request.body())
                        .url(modifiedUrl.build())
                        .build();

            } else if (request.method().equals("POST")) {// 为POST方式统一添加请求参数
                if (request.body() instanceof FormBody) {
                    FormBody.Builder body = new FormBody.Builder();
                    if (parameters != null && parameters.size() != 0) {
                        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                            body.addEncoded(entry.getKey(), entry.getValue().toString());
                        }
                    }
                    body.build();

                    FormBody oldBody = (FormBody) request.body();
                    if (oldBody != null && oldBody.size() != 0) {
                        for (int i = 0; i < oldBody.size(); i++) {
                            body.addEncoded(oldBody.encodedName(i), oldBody.encodedValue(i));
                        }
                    }

                    request = request.newBuilder()
                            .post(body.build())
                            .build();
                }
            }
        }
        return chain.proceed(request);
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    /**
     * 参数回调接口
     */
    public interface ParameterCallback {

        HashMap<String, Object> parameters();

        ParameterCallback DEFAULT = new ParameterCallback() {

            @Override
            public HashMap<String, Object> parameters() {
                return null;
            }
        };
    }
}