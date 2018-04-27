package me.mvp.frame.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.BasePresenter;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.integration.cache.CacheType;
import me.mvp.frame.utils.AppUtils;

/**
 * BaseFragment
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IFragment<P> {

    // AppComponent
    protected AppComponent component = null;

    // Cache
    private Cache<String, Object> cache;

    // Presenter
    protected P presenter = null;

    public BaseFragment() {
        setArguments(new Bundle());
    }

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (cache == null) {
            cache = AppUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return cache;
    }

    @Override
    public void setComponent(AppComponent component) {
        this.component = component;
    }

    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    // Fragment当前状态是否可见
    private boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 当前Fragment是可见的
     */
    protected void onVisible() {

    }

    /**
     * 当前Fragment是不可见的
     */
    protected void onInvisible() {

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (presenter == null) {
            presenter = obtainPresenter();
        }
    }

    /**
     * 创建Fragment中显示的view,其中inflater用来装载布局文件，container表示<fragment>标签的父标签对应的ViewGroup对象，savedInstanceState可以获取Fragment保存的状态
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }
}