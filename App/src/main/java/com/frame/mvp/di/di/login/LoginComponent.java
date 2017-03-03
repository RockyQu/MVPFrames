package com.frame.mvp.di.di.login;

import com.frame.mvp.di.common.component.AppComponent;
import com.frame.mvp.mvp.login.LoginActivity;
import com.tool.common.di.scope.ActivityScope;

import dagger.Component;

/**
 * LoginComponent
 */
@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {

    void inject(LoginActivity activity);
}