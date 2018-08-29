package me.mvp.frame.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.InflateException;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.BasePresenter;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.integration.cache.CacheType;
import me.mvp.frame.utils.AppUtils;

/**
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 {@link Activity} 的三方库, 那你就需要自己自定义 {@link Activity}
 * 继承于这个特定的 {@link Activity}, 然后再按照 {@link BaseActivity} 的格式, 将代码复制过去, 必须要实现{@link IActivity} 接口
 */
public abstract class BaseActivity<P extends BasePresenter, B extends ViewDataBinding> extends AppCompatActivity implements IActivity<P> {

    // AppComponent
    protected AppComponent component = null;

    // Cache
    private Cache<String, Object> cache;

    // Presenter
    @Nullable
    protected P presenter;

    /**
     * 控件引用，使用方法 view.{id}
     */
    protected B view;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (cache == null) {
            cache = AppUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return cache;
    }

    @Override
    public void setComponent(AppComponent component) {
        this.component = component;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutId = getLayoutId();
            if (layoutId != 0) {
                setContentView(layoutId);

                view = DataBindingUtil.setContentView(this, getLayoutId());
            }
        } catch (Exception e) {
            if (e instanceof InflateException) {
                throw e;
            }
            e.printStackTrace();
        }

        if (presenter == null) {
            presenter = obtainPresenter();
        }

        this.create(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (presenter == null) {
            presenter = obtainPresenter();
        }
    }

    @Override
    public void onDestroy() {
        if (view != null) {
            view.unbind();
        }
        super.onDestroy();

        // 释放资源
        if (presenter != null) {
            presenter.onDestroy();
        }

        this.presenter = null;
    }

    @Override
    public boolean useFragment() {
        return true;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }
}