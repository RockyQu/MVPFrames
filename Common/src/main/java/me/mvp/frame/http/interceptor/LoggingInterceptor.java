package me.mvp.frame.http.interceptor;

import com.logg.Logg;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
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

    private List<Logger> loggers = new ArrayList<>();

    public LoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public LoggingInterceptor(Logger logger) {
        loggers.add(logger);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
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
        log(requestStartMessage);

        // Content-Type
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                log("Content-Type: " + requestBody.contentType());
            }
            if (requestBody.contentLength() != -1) {
                log("Content-Length: " + requestBody.contentLength());
            }
        }

        // 拼装请求参数
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                log(name + ": " + headers.value(i));
            }
        }

        // Request结束
        if (!hasRequestBody) {
            log("--> END " + request.method());
        } else if (bodyEncoded(request.headers())) {
            log("--> END " + request.method() + " (encoded body omitted)");
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isPlaintext(buffer)) {
                log(buffer.readString(charset));
                log("--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            } else {
                log("--> END " + request.method() + " (binary "
                        + requestBody.contentLength() + "-byte body omitted)");
            }
        }

        // Response开始
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            log("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        log("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (", " + bodySize + " body") + ')');

        headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            log(headers.name(i) + ": " + headers.value(i));
        }

        if (!HttpHeaders.hasBody(response)) {
            log("<-- END HTTP");
        } else if (bodyEncoded(response.headers())) {
            log("<-- END HTTP (encoded body omitted)");
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
                    log("Couldn't decode the response body; charset is likely malformed.");
                    log("<-- END HTTP");
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return response;
            }

            if (contentLength != 0) {
                log(buffer.clone().readString(charset));
            }

            log("<-- END HTTP (" + buffer.size() + "-byte body)");
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

    private void log(String message) {
        for (Logger logger : loggers) {
            logger.log(message);
        }
    }

    public interface Logger {
        void log(String message);

        Logger DEFAULT = new Logger() {

            @Override
            public void log(String message) {
                Logg.e(message);
            }
        };
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    public void removeLogger(Logger logger) {
        loggers.remove(logger);
    }
}