package com.tool.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tool.common.http.ParameterBody;
import com.tool.common.utils.base.BaseUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * HttpUtils
 */
public class HttpUtils extends BaseUtils {

    public HttpUtils() {
        super();
    }

    /**
     * 用来解析请求参数，将ParameterBody转成RequestBody
     */
    public static Map<String, RequestBody> body(Map<String, ParameterBody> params) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (Map.Entry<String, ParameterBody> entry : params.entrySet()) {
            String key = entry.getKey();
            ParameterBody body = entry.getValue();
            if (!TextUtils.isEmpty(body.getValue())) {// 文本类型
                requestBodyMap.put(key, RequestBody.create(null, body.getValue()));
            } else if (body.getFile() != null && body.getFile().exists()) {// 文件类型
                File file = body.getFile();
                requestBodyMap.put(body.getKey() + "\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
        }

        return requestBodyMap;
    }

    /**
     * 用来解析请求参数，将ParameterBody转成Object
     */
    public static Map<String, Object> form(Map<String, ParameterBody> params) {
        Map<String, Object> requestFormMap = new HashMap<>();
        for (Map.Entry<String, ParameterBody> entry : params.entrySet()) {
            String key = entry.getKey();
            ParameterBody body = entry.getValue();
            if (!TextUtils.isEmpty(body.getValue())) {// 文本类型
                requestFormMap.put(key, entry.getValue().getValue());
            }
        }

        return requestFormMap;
    }
}