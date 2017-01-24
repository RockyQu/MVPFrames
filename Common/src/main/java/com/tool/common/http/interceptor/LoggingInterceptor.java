package com.tool.common.http.interceptor;

import com.tool.common.log.QLog;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Http日志拦截器
 */
public class LoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private volatile Level level = Level.BODY;

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         */
        BODY
    }

    public LoggingInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        // 请求地址
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        QLog.e(requestStartMessage);

        // Content-Type
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                QLog.e("Content-Type: " + requestBody.contentType());
            }
            if (requestBody.contentLength() != -1) {
                QLog.e("Content-Length: " + requestBody.contentLength());
            }
        }

        // 拼装请求参数
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                QLog.e(name + ": " + headers.value(i));
            }
        }

        // Request结束
        if (!hasRequestBody) {
            QLog.e("--> END " + request.method());
        } else if (bodyEncoded(request.headers())) {
            QLog.e("--> END " + request.method() + " (encoded body omitted)");
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
                QLog.e(buffer.readString(charset));
                QLog.e("--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            } else {
                QLog.e("--> END " + request.method() + " (binary "
                        + requestBody.contentLength() + "-byte body omitted)");
            }
        }

        // Response开始
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            QLog.e("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        QLog.e("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (", " + bodySize + " body") + ')');

        headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            QLog.e(headers.name(i) + ": " + headers.value(i));
        }

        if (!HttpHeaders.hasBody(response)) {
            QLog.e("<-- END HTTP");
        } else if (bodyEncoded(response.headers())) {
            QLog.e("<-- END HTTP (encoded body omitted)");
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    QLog.e("Couldn't decode the response body; charset is likely malformed.");
                    QLog.e("<-- END HTTP");
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                QLog.e("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return response;
            }

            if (contentLength != 0) {
                QLog.e(buffer.clone().readString(charset));
            }

            QLog.e("<-- END HTTP (" + buffer.size() + "-byte body)");
        }
        // Response结束

        return response;
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

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}