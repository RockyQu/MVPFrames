package com.frame.mvp.mvp.login;

import android.util.Log;

import com.tool.common.frame.BasePresenter;

/**
 * LoginPresenter
 */
public class LoginPresenter extends LoginContract.Presenter {

    @Override
    public void login(String account, String password) {
        Log.e("login", "login");

        view.loginSuccess();
    }

}
