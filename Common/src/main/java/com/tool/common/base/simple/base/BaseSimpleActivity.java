package com.tool.common.base.simple.base;

import android.support.v7.app.AppCompatActivity;

import com.tool.common.base.delegate.IActivity;
import com.tool.common.base.simple.delegate.ISimpleActivity;
import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.BasePresenter;

import javax.inject.Inject;

/**
 * BaseSimpleActivity
 */
public abstract class BaseSimpleActivity<P extends BasePresenter> extends AppCompatActivity implements ISimpleActivity {

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
    protected void onDestroy() {
        super.onDestroy();

        // 释放资源
        if (presenter != null) {
            presenter.onDestroy();
        }

        this.presenter = null;
    }
}