package com.tool.common.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.tool.common.frame.BasePresenter;
import com.tool.common.utils.ClassUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    /**
     * Presenter
     */
    @Inject
    protected P presenter = null;

    // 解除绑定
    private Unbinder unbinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());

        // 绑定ButterKnife
        unbinder = ButterKnife.bind(this);

//        presenter = ClassUtils.getT(this);
//        if (this instanceof BaseView) presenter.setVM(this, mModel);

        this.create(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 解除绑定
        unbinder.unbind();

        // 释放资源
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 获取布局文件，需要在子类中重写此方法
     */
    public abstract int getLayoutId();

    /**
     * 相当于Activity onCreate方法，需要在子类中重写此方法
     */
    public abstract void create(Bundle savedInstanceState);

    /**
     * 依赖注入
     */
    protected abstract void componentInject();
}