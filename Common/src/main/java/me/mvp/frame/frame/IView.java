package me.mvp.frame.frame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 这里除了定义 {@link #handleMessage} 还可以定义一些比较常用,每个 {@link View} 都会用到的方法
 * 因为 {@link View} 的实现类可能会是 {@link Activity}, {@link Fragment} 或者 {@link Dialog} 以及一些自定义 {@link View}
 * 所以不能定义一些某个类特有的方法比如 {@link Activity#startActivity(Intent)} 就是 {@link Activity} 特有的
 * 其他 {@link View} 实现类并不一定具备这个功能
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
     * @param type    类型
     * @param message 提示信息
     */
    void showMessage(int type, @NonNull String message);

    /**
     * 处理消息,这里面和handler的原理一样,通过swith(what)做不同的操作
     *
     * @param message
     */
    void handleMessage(@NonNull Message message);
}