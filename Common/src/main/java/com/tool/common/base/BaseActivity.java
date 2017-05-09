package com.tool.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.BasePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity{

    // AppComponent
    protected AppComponent component = null;

    /**
     * Presenter
     */
    @Inject
    protected P presenter;

    // 解除绑定
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());

        component = ((App) getApplicationContext()).getAppComponent();

        this.setupActivityComponent(component);

        this.create(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        // 绑定ButterKnife
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 如使用MVP结构，子类需实现此方法初始化Presenter
     */
    protected void setupActivityComponent(AppComponent component) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放资源
        if (presenter != null) {
            presenter.onDestroy();
        }

        // 解除绑定
        if (unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }

        this.presenter = null;
        this.unbinder = null;
    }

    /**
     * 获取布局文件，需要在子类中重写此方法
     */
    public abstract int getLayoutId();

    /**
     * 相当于Activity onCreate方法，需要在子类中重写此方法
     */
    public abstract void create(Bundle savedInstanceState);
}