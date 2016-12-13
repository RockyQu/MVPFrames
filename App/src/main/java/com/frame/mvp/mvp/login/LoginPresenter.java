package com.frame.mvp.mvp.login;

import android.util.Log;

import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BasePresenter;

import javax.inject.Inject;

/**
 * LoginPresenter
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model,LoginContract.View> {

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View view) {
        super(model,view);
    }

    public void login(String account, String password) {
        Log.e("login","login");

        view.loginSuccess();
    }

}
