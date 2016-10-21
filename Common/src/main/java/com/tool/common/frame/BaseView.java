package com.tool.common.frame;

import android.content.Intent;

/**
 * 常用基本View方法定义到BaseView中。
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
     * @param flag     跳转类型索引（由业务逻辑的差异可能转到不同Activity）
     */
    void startActivity(Intent intent, int flag);

    /**
     * 关闭Activity
     */
    void finishActivity();
}
