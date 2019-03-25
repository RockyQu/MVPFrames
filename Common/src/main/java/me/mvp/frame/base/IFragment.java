package me.mvp.frame.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.IPresenter;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.integration.cache.LruCache;

/**
 * IFragment
 */
public interface IFragment<P extends IPresenter> {

    /**
     * 提供在 {@link Fragment} 生命周期内的缓存容器，可向此 {@link Fragment} 存取一些必要的数据
     * 此缓存容器和 {@link Fragment} 的生命周期绑定，如果 {@link Fragment} 在屏幕旋转或者配置更改的情况下
     * 重新创建，那此缓存容器中的数据也会被清空
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
    void create(@Nullable Bundle savedInstanceState);

    /**
     * 使用EventBus
     *
     * @return 是否使用EventBus
     */
    boolean useEventBus();
}