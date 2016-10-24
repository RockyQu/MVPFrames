package com.tool.common.frame;

import android.content.Intent;

/**
 * 常用基本View方法定义到BaseView中
 */
public interface BaseView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示提示信息
     *
     * @param message 提示信息
     */
    void showMessage(String message);

    /**
     * 跳转Activity
     *
     * @param intent   Intent
     */
    void startActivity(Intent intent);

    /**
     * 关闭Activity
     */
    void finishActivity();
}
