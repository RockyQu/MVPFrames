package me.mvp.frame.base.delegate;

import android.os.Bundle;

import me.mvp.frame.di.component.AppComponent;

/**
 * IFragment
 */
public interface IFragment {

    /**
     * 在Base基类实现些方法，为了能够方便的获取到{@link AppComponent}
     *
     * @param component
     */
    void setComponent(AppComponent component);

    /**
     * 提供AppComponent给实现类，进行Component依赖
     *
     * @param component
     */
    void setupFragmentComponent(AppComponent component);

    /**
     * 布局ID
     *
     * @return
     */
    int getLayoutId();

    /**
     * Init
     *
     * @param savedInstanceState
     */
    void create(Bundle savedInstanceState);

    /**
     * 使用EventBus
     *
     * @return
     */
    boolean useEventBus();
}