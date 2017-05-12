package com.frame.mvp.di.main;

import com.frame.mvp.mvp.login.LoginContract;
import com.frame.mvp.mvp.login.LoginModel;
import com.frame.mvp.mvp.main.MainContract;
import com.frame.mvp.mvp.main.MainModel;
import com.tool.common.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * MainModule
 */
@Module
public class MainModule {

    private MainContract.View view;

    public MainModule(MainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MainContract.View provideView() {
        return view;
    }

    @ActivityScope
    @Provides
    MainContract.Model provideModel(MainModel model) {
        return model;
    }
}
