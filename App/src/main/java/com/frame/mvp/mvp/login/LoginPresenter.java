package com.frame.mvp.mvp.login;

import android.app.Application;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.entity.User;
import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BasePresenter;
import com.tool.common.http.RetrofitCallback;
import com.tool.common.http.ResponseEntity;
import com.tool.common.http.exception.ApiException;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * LoginPresenter
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {

    // Application
    private MVPApplication application;

    // Login User
    private Call<ResponseEntity<User>> user;

    @Inject
    public LoginPresenter(Application application, LoginContract.Model model, LoginContract.View view) {
        super(model, view);
        this.application = (MVPApplication) application;
    }

    public void login(final String account, final String password) {
        view.showLoading();
        user = model.login(account, password);
        user.enqueue(new RetrofitCallback<ResponseEntity<User>>(application) {

            @Override
            public void onResponse(ResponseEntity<User> body) {
                super.onResponse(body);
            }

            @Override
            public void onFailure(ApiException exception) {
                super.onFailure(exception);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
//        user.enqueue(new RetrofitCallback<ResponseEntity<User>>(application) {
//
//            @Override
//            protected void onResponse(ResponseEntity<User> body) {
//                User user = body.getData();
//                if (user != null) {
//                    user.setAccount(account);
//                    user.setPassword(password);
//
//                    String value = GsonUtils.getString(user);
//                    if (value != null) {
//                        PreferencesUtils.putString(application, LoginActivity.class.getName(), value);
//
//                        view.showMessage(body.getMessage());
//                    }
//
//                    application.getAppComponent().extras().put(LoginActivity.class.getName(), user);
//                    view.finishActivity();
//                }
//            }
//
//            @Override
//            protected void onFailure(String message) {
//                view.showMessage(message);
//            }
//
//            @Override
//            protected void onFinish() {
//                view.hideLoading();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (user != null) {
            user.cancel();
        }
    }
}