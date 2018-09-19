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
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @{@link Fragment} 的三方库, 那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment}, 然后再按照 {@link BaseFragment} 的格式, 将代码复制过去, 必须要实现{@link IFragment} 接口
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IFragment<P> {

    // AppComponent
    protected AppComponent component = null;

    // Cache
    private Cache<String, Object> cache;

    // Presenter
    protected P presenter = null;

    // Fragment当前状态是否可见
    private boolean isVisible;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 当 Fragment 可见时自动调用此方法，你可以在子类中重写此方法来添加自己的逻辑代码
     */
    protected void onVisible() {

    }

    /**
     * 当 Fragment 不可见时自动调用此方法，你可以在子类中重写此方法来添加自己的逻辑代码
     */
    protected void onInvisible() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (presenter == null) {
            presenter = obtainPresenter();
        }

        this.create(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (presenter == null) {
            presenter = obtainPresenter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.onDestroy();
        }

        this.presenter = null;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }
}