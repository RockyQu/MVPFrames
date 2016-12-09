package com.frame.mvp.mvp.login;

import com.frame.mvp.mvp.login.LoginContract;
import com.tool.common.frame.BasePresenter;

/**
 * LoginPresenter
 */
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {


    public LoginPresenter(LoginContract.Model model, LoginContract.View view) {
        super(model, view);
    }

    public void login(String name, String password) {

    }

}
