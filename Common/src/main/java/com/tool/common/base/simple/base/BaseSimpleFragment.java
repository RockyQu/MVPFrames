package com.tool.common.base.simple.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tool.common.base.simple.delegate.ISimpleFragment;
import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.BasePresenter;

/**
 * BaseSimpleFragment
 */
public abstract class BaseSimpleFragment<P extends BasePresenter> extends Fragment implements ISimpleFragment<P> {

    // AppComponent
    protected AppComponent component = null;

    /**
     * Presenter
     */
    protected P presenter = null;

    public BaseSimpleFragment() {
        setArguments(new Bundle());
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

    /**
     * 当Fragment不再被使用时，如按返回键，就会调用此方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放资源
        if (presenter != null) {
            presenter.onDestroy();
        }

        this.presenter = null;
    }
}