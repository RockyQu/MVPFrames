package com.tool.common.http.interceptor;

import android.app.Application;

import com.tool.common.base.BaseApplication;
import com.tool.common.http.NetworkHandler;
import com.tool.common.http.receiver.NetworkStatusReceiver;
import com.tool.common.utils.ZipUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Http拦截器
 */
@Singleton
public class NetworkInterceptor implements Interceptor {

    private Application application;
    private NetworkHandler handler;

    @Inject
    public NetworkInterceptor(Application application, NetworkHandler handler) {
        this.application = application;
        this.handler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        // 在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不需要操作则返回参数中的request
        if (handler != null) {
            request = handler.onHttpRequest(chain, request);
        }

        NetworkStatusReceiver.Type type = NetworkStatusReceiver.getType(application);
        if (type == NetworkStatusReceiver.Type.NONE) {// 无网络时强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，会获取不到缓存）
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);
        if (type == NetworkStatusReceiver.Type.MOBILE || type == NetworkStatusReceiver.Type.WIFI) {
            int maxAge = 5;// 10秒
            originalResponse = originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxTime = 60 * 60; // 1小时
            originalResponse = originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                    .build();
        }

        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        //获取content的压缩类型
        String encoding = originalResponse.headers().get("Content-Encoding");

        Buffer clone = buffer.clone();

        String bodyString;

        //解析response content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {// content使用gzip压缩
            bodyString = ZipUtils.decompressForGzip(clone.readByteArray());
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {// content使用zlib压缩
            bodyString = ZipUtils.decompressToStringForZlib(clone.readByteArray());
        } else {// content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }

        // 这里可以提前一步拿到服务器返回的结果,外部实现此接口可以做一些操作，比如Token超时，重新获取
        if (handler != null) {
            return handler.onHttpResponse(bodyString, chain, request, originalResponse);
        }

        return originalResponse;
    }
}