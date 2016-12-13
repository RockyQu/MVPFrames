package com.tool.common.utils;

import com.tool.common.utils.base.BaseUtils;

import java.lang.reflect.ParameterizedType;

/**
 * ClassUtils
 */
public class ClassUtils extends BaseUtils {

    public ClassUtils() {
        super();
    }

    public static <T> T getT(Object object) {
        return getT(object, 0);
    }

    public static <T> T getT(Object object, int index) {
        try {
            return ((Class<T>) ((ParameterizedType) (object.getClass().getGenericSuperclass())).getActualTypeArguments()[index]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}