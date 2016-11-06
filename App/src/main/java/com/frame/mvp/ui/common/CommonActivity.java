package com.frame.mvp.ui.common;

import android.os.Bundle;
import android.view.View;

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
    }
}
