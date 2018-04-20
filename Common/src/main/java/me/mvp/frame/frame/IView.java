package me.mvp.frame.frame;

import android.content.Intent;

/**
 * 常用基本View方法定义到BaseView中
 */
public interface IView {

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
     * @param type 类型
     * @param message 提示信息
     */
    void showMessage(int type, String message);

    /**
     * 跳转Activity
     *
     * @param intent Intent
     */
    void launchActivity(Intent intent);

    /**
     * 关闭Activity
     */
    void finishActivity();
}
