package me.mvp.frame.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.mvp.frame.base.delegate.IFragment;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.BasePresenter;

import javax.inject.Inject;

/**
 * Fragment
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IFragment {

    // AppComponent
    protected AppComponent component = null;

    /**
     * Presenter
     */
    @Inject
    protected P presenter = null;

    public BaseFragment() {
        setArguments(new Bundle());
    }

    @Override
    public void setComponent(AppComponent component) {
        this.component = component;
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