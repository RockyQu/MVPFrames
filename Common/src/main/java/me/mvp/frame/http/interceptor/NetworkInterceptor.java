package me.mvp.frame.http.interceptor;

import android.app.Application;
import android.support.annotation.Nullable;

import me.logg.Logg;
import me.logg.config.LoggConstant;
import me.mvp.frame.http.NetworkInterceptorHandler;
import me.mvp.frame.utils.ZipUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

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
    NetworkInterceptorHandler handler;

    // HTTP 日志级别
    @Inject
    Level level;

    public enum Level {
        NONE,// 不打印任何日志
        ALL// 打印全部日志
    }

    @Inject
    public NetworkInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        // Request
        Request request = chain.request();

        // 解析 Request 日志
        this.resolveRequestLogger(new StringBuilder(), chain, request);

        // Response
        Response response = chain.proceed(chain.request());

        // 解析 Response 日志
        this.resolveResponseLogger(new StringBuilder(), chain, response);

        // 读取服务器返回结果
        ResponseBody responseBody = response.body();

        // 解析服务器返回结果
        String bodyString = null;
        if (responseBody != null && isParseable(responseBody.contentType())) {
            bodyString = this.resolveResult(request, response);
        }

        // 这里可以提前一步拿到服务器返回的结果，外部实现此接口可以做一些操作，比如 Token 超时，重新获取
        if (handler != null) {
            return handler.onHttpResponse(bodyString, chain, request, response);
        }

        return response;
    }

    /**
     * 解析 Request 日志
     *
     * @param builder
     * @param chain
     */
    private Request resolveRequestLogger(StringBuilder builder, Chain chain, Request request) throws IOException {
        if (level == Level.NONE) {
            return request;
        }

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();

        builder.append(LoggConstant.SPACE).append(LoggConstant.BR);
        builder
                .append("--> ")
                .append(request.method())
                .append(' ')
                .append(request.url())
                .append(connection != null ? " " + connection.protocol() : "")
        ;

        if (hasRequestBody) {
            builder
                    .append(" (")
                    .append(requestBody.contentLength())
                    .append("-byte body)");
        }

        builder.append(LoggConstant.BR);

        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                builder.append("Content-Type: ").append(requestBody.contentType()).append(LoggConstant.BR);
            }
            if (requestBody.contentLength() != -1) {
                builder.append("Content-Length: ").append(requestBody.contentLength()).append(LoggConstant.BR);
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                builder.append(name).append(": ").append(headers.value(i)).append(LoggConstant.BR);
            }
        }

        if (!hasRequestBody) {
            builder.append("--> END ").append(request.method()).append(LoggConstant.BR);
        } else if (bodyHasUnknownEncoding(request.headers())) {
            builder.append("--> END ").append(request.method()).append(" (encoded body omitted)").append(LoggConstant.BR);
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
                builder.append(buffer.readString(charset)).append(LoggConstant.BR);
                builder
                        .append("--> END ")
                        .append(request.method())
                        .append(" (")
                        .append(requestBody.contentLength())
                        .append("-byte body)")
                        .append(LoggConstant.BR)
                ;
            } else {
                builder
                        .append("--> END ")
                        .append(request.method())
                        .append(" (binary ")
                        .append(requestBody.contentLength())
                        .append("-byte body omitted)")
                        .append(LoggConstant.BR)
                ;
            }
        }

        Logg.e("OkHttp", builder.toString());
        return request;
    }

    /**
     * 解析 Response 日志
     *
     * @param builder
     * @param chain
     */
    private Response resolveResponseLogger(StringBuilder builder, Chain chain, Response response) throws IOException {
        long startNs = System.nanoTime();

        if (level == Level.NONE) {
            return response;
        }

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        boolean hasResponseBody = responseBody != null;

        if (hasResponseBody) {
            long contentLength = responseBody.contentLength();

            builder.append(LoggConstant.SPACE).append(LoggConstant.BR);
            builder
                    .append("<-- ")
                    .append(response.code())
                    .append(response.message().isEmpty() ? "" : ' ' + response.message())
                    .append(' ')
                    .append(response.request().url())
                    .append(" (")
                    .append(tookMs)
                    .append("ms")
                    .append(", ")
                    .append(contentLength != -1 ? contentLength + "-byte" : "unknown-length")
                    .append(" body")
                    .append(')')
                    .append(LoggConstant.BR)
            ;

            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                builder.append(headers.name(i)).append(": ").append(headers.value(i)).append(LoggConstant.BR);
            }

            if (!HttpHeaders.hasBody(response)) {
                builder.append("<-- END HTTP").append(LoggConstant.BR);
            } else if (bodyHasUnknownEncoding(response.headers())) {
                builder.append("<-- END HTTP (encoded body omitted)").append(LoggConstant.BR);
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Long gzippedLength = null;
                if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                    gzippedLength = buffer.size();
                    GzipSource gzippedResponseBody = null;
                    try {
                        gzippedResponseBody = new GzipSource(buffer.clone());
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    } finally {
                        if (gzippedResponseBody != null) {
                            gzippedResponseBody.close();
                        }
                    }
                }

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    builder.append("<-- END HTTP (binary ").append(buffer.size()).append("-byte body omitted)").append(LoggConstant.BR);
                    return response;
                }

                if (contentLength != 0) {
                    builder.append(buffer.clone().readString(charset)).append(LoggConstant.BR);
                }

                if (gzippedLength != null) {
                    builder.append("<-- END HTTP (").append(buffer.size()).append("-byte, ").append(gzippedLength).append("-gzipped-byte body)").append(LoggConstant.BR);
                } else {
                    builder.append("<-- END HTTP (").append(buffer.size()).append("-byte body)").append(LoggConstant.BR);
                }
            }

        }

        Logg.e("OkHttp", builder.toString());
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

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    /**
     * @param headers
     * @return boolean
     */
    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }
}