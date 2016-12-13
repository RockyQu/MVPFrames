package com.frame.mvp.ui.common;

import com.frame.mvp.app.MVPApplication;
import com.tool.common.base.BaseActivity;
import com.tool.common.frame.BaseModel;
import com.tool.common.frame.BasePresenter;

/**
 * CommonActivity
 */
public abstract class CommonActivity<P extends BasePresenter, M extends BaseModel> extends BaseActivity<P, M> {

    // Application
    protected MVPApplication application = null;

    @Override
    protected void componentInject() {
        application = (MVPApplication) getApplication();
    }
}
