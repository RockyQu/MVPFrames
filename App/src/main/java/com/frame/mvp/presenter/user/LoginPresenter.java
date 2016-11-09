package com.frame.mvp.presenter.user;

import com.frame.mvp.contract.user.LoginContract;
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
