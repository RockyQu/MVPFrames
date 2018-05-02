package me.mvp.frame.http.interceptor;

import android.app.Application;
import android.support.annotation.Nullable;

import me.mvp.frame.http.NetworkInterceptorHandler;
import me.mvp.frame.http.receiver.NetworkStatusReceiver;
import me.mvp.frame.utils.ZipUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.CacheControl;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Http 拦截器
 */
@Singleton
public class NetworkInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Inject
    Application application;

    // 通信拦截器回调接口
    @Inject
    NetworkInterceptorHandler networkInterceptorHandler;

    // HTTP 日志级别
    @Inject
    Level level;

    public enum Level {
        NONE,// 不打印任何日志
        SIMPLE,// 只打印关键日志
        ALL// 打印全部日志
    }

    @Inject
    public NetworkInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        // 日志信息
        StringBuilder logBuilder = new StringBuilder();

        // 解析 Request 日志
        Request request = this.resolveRequestLogger(logBuilder, chain);

        // 在请求服务器之前可以拿到 Request，做一些操作比如给 Request 添加 Header，如果不需要操作则返回参数中的 Request
        if (networkInterceptorHandler != null) {
            request = networkInterceptorHandler.onHttpRequest(chain, request);
        }

        // 解析 Response 日志
        Response response = this.resolveResponseLogger(logBuilder, chain);

        // 读取服务器返回结果
        ResponseBody responseBody = response.body();

        // 解释服务器返回结果
        String bodyString = null;
        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = this.resolveResult(request, response);
        }

        // 这里可以提前一步拿到服务器返回的结果，外部实现此接口可以做一些操作，比如 Token 超时，重新获取
        if (networkInterceptorHandler != null) {
            return networkInterceptorHandler.onHttpResponse(bodyString, chain, request, response);
        }

        return response;
    }

    /**
     * 解析 Request 日志
     *
     * @param logBuilder
     * @param chain
     */
    private Request resolveRequestLogger(StringBuilder logBuilder, Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");

        if (requestBody != null) {
            requestStartMessage += " (" + request.body().contentLength() + "-byte body)";
        }

        return request;
    }

    /**
     * Resolve Response Logger
     *
     * @param logBuilder
     * @param chain
     */
    private Response resolveResponseLogger(StringBuilder logBuilder, Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        return response;
    }

    /**
     * 打印响应结果
     *
     * @param request
     * @param response
     * @return String
     * @throws IOException
     */
    @Nullable
    private String resolveResult(Request request, Response response) throws IOException {
        try {
            ResponseBody responseBody = response.newBuilder().build().body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            // 获取 content 的压缩类型
            String encoding = response
                    .headers()
                    .get("Content-Encoding");

            Buffer clone = buffer.clone();

            return parseContent(responseBody, encoding, clone);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody
     * @param encoding
     * @param clone
     * @return String
     */
    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            return ZipUtils.decompressForGzip(clone.readByteArray(), convertCharset(charset));
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {
            return ZipUtils.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));
        } else {// content没有被压缩
            return clone.readString(charset);
        }
    }

    /**
     * 是否可以解析
     *
     * @param mediaType
     * @return boolean
     */
    private boolean isParseable(MediaType mediaType) {
        return isText(mediaType) || isPlain(mediaType) || isJson(mediaType) || isForm(mediaType) || isHtml(mediaType) || isXml(mediaType);
    }

    /**
     * MediaType is Text
     *
     * @param mediaType
     * @return boolean
     */
    private boolean isText(MediaType mediaType) {
        return mediaType != null && mediaType.type() != null && mediaType.type().equals("text");
    }

    /**
     * MediaType is Plain
     *
     * @param mediaType
     * @return boolean
     */
    private boolean isPlain(MediaType mediaType) {
        return mediaType != null && mediaType.subtype() != null && mediaType.subtype().toLowerCase().contains("plain");
    }

    /**
     * MediaType is Json
     *
     * @param mediaType
     * @return boolean
     */
    private boolean isJson(MediaType mediaType) {
        return mediaType != null && mediaType.subtype() != null && mediaType.subtype().toLowerCase().contains("json");
    }

    /**
     * MediaType is Xml
     *
     * @param mediaType
     * @return boolean
     */
    private boolean isXml(MediaType mediaType) {
        return mediaType != null && mediaType.subtype() != null && mediaType.subtype().toLowerCase().contains("xml");
    }

    /**
     * MediaType is Html
     *
     * @param mediaType
     * @return boolean
     */
    private boolean isHtml(MediaType mediaType) {
        return mediaType != null && mediaType.subtype() != null && mediaType.subtype().toLowerCase().contains("html");
    }

    /**
     * MediaType is Form
     *
     * @param mediaType
     * @return boolean
     */
    private boolean isForm(MediaType mediaType) {
        return mediaType != null && mediaType.subtype() != null && mediaType.subtype().toLowerCase().contains("x-www-form-urlencoded");
    }

    /**
     * @param charset
     * @return String
     */
    private String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1) {
            return s;
        }

        return s.substring(i + 1, s.length() - 1);
    }
}