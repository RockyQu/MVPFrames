package com.tool.common.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.logg.Logg;
import com.tool.common.base.simple.delegate.ISimpleFragment;
import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.IPresenter;
import com.tool.common.utils.AppUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * FragmentDelegateImpl
 */
public class FragmentDelegateImpl implements FragmentDelegate {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Unbinder unbinder;

    private IFragment iFragment;
    private ISimpleFragment iSimpleFragment;

    private IPresenter iPresenter;

    public FragmentDelegateImpl(FragmentManager fragmentManager, Fragment fragment) {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;

        if (fragment instanceof IFragment) {
            this.iFragment = (IFragment) fragment;
        } else if (fragment instanceof ISimpleFragment) {
            this.iSimpleFragment = (ISimpleFragment) fragment;
        }
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppComponent component = AppUtils.obtainAppComponentFromContext(fragment.getActivity());
        // 在Base基类实现些方法，为了能够方便的获取到AppComponent
        if (iFragment != null) {
            iFragment.setComponent(component);
        } else if (iSimpleFragment != null) {
            iSimpleFragment.setComponent(component);
        }

        // 依赖注入
        if (iFragment != null) {
            iFragment.setupFragmentComponent(component);
        } else if (iSimpleFragment != null) {
            this.iPresenter = iSimpleFragment.obtainPresenter();
            iSimpleFragment.setPresenter(iPresenter);
        }
    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {

        // 绑定ButterKnife
        if (view != null) {
            unbinder = ButterKnife.bind(fragment, view);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (iFragment != null) {
            iFragment.create(savedInstanceState);
        } else if (iSimpleFragment != null) {
            iSimpleFragment.create(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        // 注册EventBus
        if (iFragment != null) {
            if (iFragment.useEventBus()) {
                EventBus.getDefault().register(fragment);
            }
        } else if (iSimpleFragment != null) {
            if (iSimpleFragment.useEventBus()) {
                EventBus.getDefault().register(fragment);
            }
        }
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
        } else if (iSimpleFragment != null) {
            if (iSimpleFragment.useEventBus()) {
                EventBus.getDefault().unregister(fragment);
            }
        }

        if (iPresenter != null) {
            iPresenter.onDestroy();
        }

        this.unbinder = null;
        this.fragmentManager = null;
        this.fragment = null;

        if (iFragment != null) {
            this.iFragment = null;
        }
        if (iSimpleFragment != null) {
            this.iSimpleFragment = null;
        }

        this.iPresenter = null;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected FragmentDelegateImpl(Parcel in) {
        this.fragmentManager = in.readParcelable(FragmentManager.class.getClassLoader());
        this.fragment = in.readParcelable(Fragment.class.getClassLoader());
        this.unbinder = in.readParcelable(Unbinder.class.getClassLoader());

        if (iFragment != null) {
            this.iFragment = in.readParcelable(IFragment.class.getClassLoader());
        }
        if (iSimpleFragment != null) {
            this.iSimpleFragment = in.readParcelable(ISimpleFragment.class.getClassLoader());
        }

        if (iSimpleFragment != null) {
            this.iPresenter = in.readParcelable(IPresenter.class.getClassLoader());
        }
    }

    public static final Creator<FragmentDelegateImpl> CREATOR = new Creator<FragmentDelegateImpl>() {

        @Override
        public FragmentDelegateImpl createFromParcel(Parcel source) {
            return new FragmentDelegateImpl(source);
        }

        @Override
        public FragmentDelegateImpl[] newArray(int size) {
            return new FragmentDelegateImpl[size];
        }
    };
}