package com.tool.common.http.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 异常类型
 */
public class CodeException {

    // HTTP异常
    public static final int HTTP_ERROR = 0x1;
    // 网络异常
    public static final int NETWORD_ERROR = 0x2;
    // Json解析异常
    public static final int JSON_ERROR = 0x3;
    // 无法解析该域名
    public static final int UNKOWNHOST_ERROR = 0x4;
    // 未知异常
    public static final int UNKNOWN_ERROR = 0x5;
}