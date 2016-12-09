package com.frame.mvp.mvp.login;

import android.util.Log;

/**
 * LoginPresenter
 */
public class LoginPresenter extends LoginContract.Presenter {

//    public LoginPresenter(LoginContract.Model model, LoginContract.View view) {
////        super(model,view);
//    }

    @Override
    public void login(String account, String password) {
        Log.e("login","login");

        view.loginSuccess();
    }

}
