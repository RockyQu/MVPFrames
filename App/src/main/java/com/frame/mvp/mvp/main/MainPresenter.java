package com.frame.mvp.mvp.main;

import android.app.Application;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BasePresenter;
import com.tool.common.http.ResponseCallback;
import com.tool.common.http.ResponseEntity;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;

import javax.inject.Inject;

import retrofit2.Call;

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