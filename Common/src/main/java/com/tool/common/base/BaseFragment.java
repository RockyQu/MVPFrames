package com.tool.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.BasePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    // AppComponent
    protected AppComponent component = null;

    /**
     * Presenter
     */
    @Inject
    protected P presenter = null;

    // View
    private View view = null;

    // 解除绑定
    private Unbinder unbinder = null;

    /**
     * 当fragment第一次与Activity产生关联时就会调用，以后不再调用
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        component = ((App) getContext().getApplicationContext()).getAppComponent();

        this.setupFragmentComponent(component);
    }

    /**
     * 创建Fragment中显示的view,其中inflater用来装载布局文件，container表示<fragment>标签的父标签对应的ViewGroup对象，savedInstanceState可以获取Fragment保存的状态
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);

        // 绑定ButterKnife
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    /**
     * 在Activity.onCreate方法调用后会立刻调用此方法，表示窗口已经初始化完毕，此时可以调用控件了
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.create(savedInstanceState, view);
    }

    /**
     * 如使用MVP结构，子类需实现此方法初始化Presenter
     */
    protected void setupFragmentComponent(AppComponent component) {

    }

    /**
     * 当fragment状态被保存，或者从回退栈弹出，该方法被调用
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // 解除绑定
        if (unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }
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
        this.view = null;
        this.unbinder = null;
    }

    public abstract int getLayoutId();

    public abstract void create(Bundle savedInstanceState, View view);
}