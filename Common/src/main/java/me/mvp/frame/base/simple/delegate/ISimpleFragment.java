package me.mvp.frame.base.simple.delegate;

import android.os.Bundle;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.IPresenter;

/**
 * ISimpleFragment
 */
public interface ISimpleFragment<P extends IPresenter> {

    /**
     * 在Base基类实现些方法，为了能够方便的获取到{@link AppComponent}
     *
     * @param component
     */
    void setComponent(AppComponent component);

    P obtainPresenter();

    void setPresenter(P presenter);

    /**
     * 布局ID
     *
     * @return 布局文件ID
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
     * @return 是否使用EventBus
     */
    boolean useEventBus();
}