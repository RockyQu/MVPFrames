package me.mvp.frame.base.simple.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.mvp.frame.base.simple.delegate.ISimpleActivity;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.simple.BaseSimplePresenter;

/**
 * BaseSimpleActivity
 */
public abstract class BaseSimpleActivity<P extends BaseSimplePresenter> extends AppCompatActivity implements ISimpleActivity<P> {

    // AppComponent
    protected AppComponent component = null;

    /**
     * Presenter
     */
    protected P presenter;

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