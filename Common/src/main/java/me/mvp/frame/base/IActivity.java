package me.mvp.frame.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.mvp.frame.base.BaseFragment;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.IPresenter;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.integration.cache.LruCache;

/**
 * IActivity
 */
public interface IActivity<P extends IPresenter> {

    /**
     * 提供在 {@link Activity} 生命周期内的缓存容器，可向此 {@link Activity} 存取一些必要的数据
     * 此缓存容器和 {@link Activity} 的生命周期绑定，如果 {@link Activity} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空
     *
     * @return like {@link LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 在Base基类实现些方法，为了能够方便的获取到{@link AppComponent}
     *
     * @param component
     */
    void setComponent(@Nullable AppComponent component);

    @Nullable
    P obtainPresenter();

    /**
     * 如果getLayoutId返回0，框架则不会调用{@link android.app.Activity#setContentView(int)}
     *
     * @return 布局文件ID
     */
    int getLayoutId();

    /**
     * 相当于{@link android.app.Activity#onCreate(Bundle)}方法
     */
    void create(@Nullable Bundle savedInstanceState);

    /**
     * 这个Activity是否会使用Fragment，框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false，那意味着这个Activity不需要绑定Fragment，那你再在这个Activity中绑定继承于{@link BaseFragment}的Fragment将不起任何作用
     *
     * @return 是否使用Fragment
     */
    boolean useFragment();

    /**
     * 使用EventBus
     *
     * @return 是否使用EventBus
     */
    boolean useEventBus();
}