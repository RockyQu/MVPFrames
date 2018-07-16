package me.mvp.frame.utils;

import com.google.gson.Gson;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.utils.base.BaseUtils;

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

    public static Gson getGson(AppComponent component) {
        return component.getGson();
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