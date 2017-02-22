package com.frame.mvp.ui.common;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.di.AppComponent;
import com.squareup.leakcanary.RefWatcher;
import com.tool.common.base.BaseFragment;
import com.tool.common.frame.BasePresenter;

/**
 * CommonFragment
 */
public abstract class CommonFragment<P extends BasePresenter> extends BaseFragment<P> {

    // Application
    protected MVPApplication application = null;

    @Override
    protected void componentInject() {
        application = (MVPApplication) getActivity().getApplication();

        // init presenter
        presenter = setupFragmentComponent(application.getAppComponent());
    }

    /**
     * 如使用MVP结构，子类需实现此方法初始化Presenter
     */
    protected <P extends BasePresenter> P setupFragmentComponent(AppComponent appComponent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher watcher = MVPApplication.getRefWatcher(getActivity());
        if (watcher != null) {
            watcher.watch(this);
        }
        this.application = null;
    }
}