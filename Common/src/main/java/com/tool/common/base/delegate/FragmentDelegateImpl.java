package com.tool.common.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tool.common.base.App;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * FragmentDelegateImpl
 */
public class FragmentDelegateImpl implements FragmentDelegate {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private IFragment iFragment;
    private Unbinder unbinder;

    public FragmentDelegateImpl(FragmentManager fragmentManager, Fragment fragment) {
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;
        this.iFragment = (IFragment) fragment;
    }

    @Override
    public void onAttach(Context context) {
        iFragment.setComponent(((App) fragment.getActivity().getApplication()).getAppComponent());
        iFragment.setupFragmentComponent(((App) fragment.getActivity().getApplication()).getAppComponent());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

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
        iFragment.create(savedInstanceState);
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
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        this.unbinder = null;
        this.fragmentManager = null;
        this.fragment = null;
        this.iFragment = null;
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
        this.iFragment = in.readParcelable(IFragment.class.getClassLoader());
        this.unbinder = in.readParcelable(Unbinder.class.getClassLoader());
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