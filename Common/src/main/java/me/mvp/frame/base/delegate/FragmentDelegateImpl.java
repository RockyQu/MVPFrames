package me.mvp.frame.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import me.mvp.frame.base.IFragment;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.frame.IPresenter;
import me.mvp.frame.utils.AppUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link FragmentDelegate} Fragment 生命周期代理实现类
 */
public class FragmentDelegateImpl implements FragmentDelegate {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Unbinder unbinder;

    private IFragment iFragment;

    private IPresenter iPresenter;

    public FragmentDelegateImpl(FragmentManager fragmentManager, Fragment fragment) {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;

        if (fragment instanceof IFragment) {
            this.iFragment = (IFragment) fragment;
        }
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 注册EventBus
        if (iFragment != null) {
            if (iFragment.useEventBus()) {
                EventBus.getDefault().register(fragment);
            }
        }

        AppComponent component = AppUtils.obtainAppComponentFromContext(fragment.getActivity());

        // 在 Base 基类实现些方法，为了能够方便的获取到AppComponent
        if (iFragment != null) {
            iFragment.setComponent(component);
        }

        // 依赖注入
        if (iFragment != null) {
            this.iPresenter = iFragment.obtainPresenter();
            iFragment.setPresenter(iPresenter);
        }
    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        if (view != null) {
            unbinder = ButterKnife.bind(fragment, view);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (iFragment != null) {
            iFragment.create(savedInstanceState);
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {
        if (unbinder != null && unbinder != unbinder.EMPTY) {
            try {
                unbinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        // 注销EventBus
        if (iFragment != null) {
            if (iFragment.useEventBus()) {
                EventBus.getDefault().unregister(fragment);
            }
        }

        if (iPresenter != null) {
            iPresenter.onDestroy();
        }

        this.unbinder = null;
        this.fragmentManager = null;
        this.fragment = null;
        this.iFragment = null;
        this.iPresenter = null;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public boolean isAdded() {
        return fragment != null && fragment.isAdded();
    }
}