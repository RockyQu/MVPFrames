package com.tool.common.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tool.common.utils.base.BaseUtils;

import java.util.List;

/**
 * GsonHelper
 */
public class GsonUtils extends BaseUtils {

    private static Gson gson;

    public GsonUtils() {
        super();
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * JsonString to Entity
     *
     * @param value
     * @param cls
     */
    public static <T> T getEntity(String value, Class<T> cls) {
        T t = null;
        try {
            t = getGson().fromJson(value, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * Object to JsonString
     *
     * @param src
     */
    public static String getString(Object src) {
        String value = null;
        try {
            value = getGson().toJson(src);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }
}