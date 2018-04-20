package me.mvp.frame.base;

import android.support.v7.app.AppCompatActivity;

import me.mvp.frame.base.delegate.IActivity;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.BasePresenter;

import javax.inject.Inject;

/**
 * BaseActivity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IActivity {

    // AppComponent
    protected AppComponent component = null;

    /**
     * Presenter
     */
    @Inject
    protected P presenter;

    @Override
    public void setComponent(AppComponent component) {
        this.component = component;
    }

    @Override
    public boolean useFragment() {
        return true;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放资源
        if (presenter != null) {
            presenter.onDestroy();
        }

        this.presenter = null;
    }
}