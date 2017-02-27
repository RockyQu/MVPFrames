package com.frame.mvp.di.login;

import com.frame.mvp.mvp.login.LoginContract;
import com.frame.mvp.mvp.login.LoginModel;
import com.tool.common.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jess on 9/4/16 11:10
 * Contact with jess.yan.effort@gmail.com
 */
@Module
public class LoginModule {

    private LoginContract.View view;

    /**
     * LoginModule,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LoginModule(LoginContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LoginContract.View provideView() {
        return view;
    }

    @ActivityScope
    @Provides
    LoginContract.Model provideModel(LoginModel model) {
        return model;
    }
}
