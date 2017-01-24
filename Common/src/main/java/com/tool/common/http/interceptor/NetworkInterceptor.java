package com.tool.common.http.interceptor;

import com.tool.common.base.BaseApplication;
import com.tool.common.utils.NetWorkUtils;
import com.tool.common.utils.ZipUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

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
public class NetworkInterceptor implements Interceptor {

    private NetworkHandler handler;

    public NetworkInterceptor(NetworkHandler handler) {
        this.handler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        // 在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不需要操作则返回参数中的request
        if (handler != null) {
            request = handler.onHttpRequest(chain, request);
        }


        if (!NetWorkUtils.isNetworkAvailable(BaseApplication.getContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);
        if (NetWorkUtils.isNetworkAvailable(BaseApplication.getContext())) {
            int maxAge = 0;
            originalResponse = originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "max-age=" + maxAge)
                    .build();
        } else {
            int maxTime = 60;
            originalResponse = originalResponse.newBuilder()
                    // 设置没有网络情况的缓存时间 
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
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
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            clone.writeTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            bodyString = ZipUtils.decompressForGzip(bytes);//解压
            outputStream.close();
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {// content使用zlib压缩
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            clone.writeTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            bodyString = ZipUtils.decompressToStringForZlib(bytes);//解压
            outputStream.close();
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }

        // 这里可以提前一步拿到服务器返回的结果,外部实现此接口可以做一些操作，比如Token超时，重新获取
        if (handler != null) {
            handler.onHttpResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    /**
     * Http通信拦截器
     */
    public interface NetworkHandler {

        /**
         * Http请求
         *
         * @param chain
         * @param request
         * @return Request
         */
        Request onHttpRequest(Interceptor.Chain chain, Request request);

        /**
         * Http响应，这里提前拿到http响应结果,可以用来判断Token是否过期
         *
         * @param result
         * @param chain
         * @param response
         * @return Response
         */
        Response onHttpResponse(String result, Interceptor.Chain chain, Response response);
    }
}