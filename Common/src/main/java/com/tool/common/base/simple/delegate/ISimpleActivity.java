package com.tool.common.base.simple.delegate;

import android.os.Bundle;

import com.tool.common.base.BaseFragment;
import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.IPresenter;

/**
 * ISimpleActivity
 */
public interface ISimpleActivity<P extends IPresenter> {

    /**
     * 在Base基类实现些方法，为了能够方便的获取到{@link AppComponent}
     *
     * @param component
     */
    void setComponent(AppComponent component);

    P obtainPresenter();

    void setPresenter(P presenter);

    /**
     * 如果getLayoutId返回0，框架则不会调用{@link android.app.Activity#setContentView(int)}
     *
     * @return
     */
    int getLayoutId();

    /**
     * 相当于{@link android.app.Activity#onCreate(Bundle)}方法
     */
    void create(Bundle savedInstanceState);

    /**
     * 这个Activity是否会使用Fragment，框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false，那意味着这个Activity不需要绑定Fragment，那你再在这个Activity中绑定继承于{@link BaseFragment}的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();
}