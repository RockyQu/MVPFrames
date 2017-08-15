package com.frame.mvp.mvp.main;

import android.app.Application;

import com.frame.mvp.app.MVPApplication;
import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BasePresenter;

import javax.inject.Inject;

/**
 * MainPresenter
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {

    // Application
    private MVPApplication application;

    @Inject
    public MainPresenter(Application application, MainContract.Model model, MainContract.View view) {
        super(model, view);
        this.application = (MVPApplication) application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}