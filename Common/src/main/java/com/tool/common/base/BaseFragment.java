package com.tool.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tool.common.frame.BasePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

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
    }

    /**
     * 在onAttach执行完后会立刻调用此方法，通常被用于读取保存的状态值，获取或者初始化一些数据
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
     * 继上面后就会调用此方法
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 在Activity.onCreate方法调用后会立刻调用此方法，表示窗口已经初始化完毕，此时可以调用控件了
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.componentInject();

        this.create(savedInstanceState, view);
    }

    /**
     * 开始执行与控件相关的逻辑代码，如按键点击
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 这是Fragment从创建到显示的最后一个回调的方法
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 当发生界面跳转时，临时暂停
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 当该方法返回时，Fragment将从屏幕上消失
     */
    @Override
    public void onStop() {
        super.onStop();
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

    /**
     * Fragment生命周期的最后一个方法，执行完后将不再与Activity关联，将释放所有fragment对象和资源
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public abstract int getLayoutId();

    public abstract void create(Bundle savedInstanceState, View view);

    /**
     * 依赖注入
     */
    protected abstract void componentInject();
}