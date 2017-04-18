package com.frame.mvp.di.di.login;

import com.frame.mvp.mvp.login.LoginActivity;
import com.tool.common.di.component.BaseComponent;
import com.tool.common.di.scope.ActivityScope;

import dagger.Component;

/**
 * LoginComponent
 */
@ActivityScope
@Component(modules = LoginModule.class, dependencies = BaseComponent.class)
public interface LoginComponent {

    void inject(LoginActivity activity);
}