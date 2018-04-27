package me.mvp.frame.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.BasePresenter;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.integration.cache.CacheType;
import me.mvp.frame.utils.AppUtils;

/**
 * BaseActivity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IActivity<P> {

    // AppComponent
    protected AppComponent component = null;

    // Cache
    private Cache<String, Object> cache;

    // Presenter
    protected P presenter;

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
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (presenter == null) {
            presenter = obtainPresenter();
        }
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