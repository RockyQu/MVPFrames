package me.mvp.frame.frame.simple;

/**
 * 常用基本View方法定义到BaseView中
 */
public interface ISimpleView {

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
    void showMessage(int type, String message);

    /**
     * 处理消息,这里面和handler的原理一样,通过swith(what)做不同的操作
     *
     * @param message
     */
    void handleMessage(Message message);
}