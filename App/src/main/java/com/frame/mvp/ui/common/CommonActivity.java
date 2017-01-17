package com.frame.mvp.ui.common;

import com.frame.mvp.app.MVPApplication;
import com.tool.common.base.BaseActivity;
import com.tool.common.frame.BasePresenter;

/**
 * CommonActivity
 */
public abstract class CommonActivity<P extends BasePresenter> extends BaseActivity<P> {

    // Application
    protected MVPApplication application = null;

    @Override
    protected void componentInject() {
        application = (MVPApplication) getApplication();

        // init presenter
        presenter = setupActivityComponent();
    }

    /**
     * 如使用MVP结构，子类需实现此方法初始化Presenter
     */
    protected <P extends BasePresenter> P setupActivityComponent() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.application = null;
    }
}