package com.frame.mvp.mvp.login;

import android.app.Application;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.entity.User;
import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BasePresenter;
import com.tool.common.http.ResponseCallback;
import com.tool.common.http.ResponseEntity;
import com.tool.common.http.exception.ApiException;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;

import javax.inject.Inject;

import retrofit2.Call;

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
        user.enqueue(new ResponseCallback<ResponseEntity<User>>() {

            @Override
            protected void onResponse(ResponseEntity<User> body) {
                User user = body.getData();
                if (user != null) {
                    user.setAccount(account);
                    user.setPassword(password);

                    String value = GsonUtils.getString(user);
                    if (value != null) {
                        PreferencesUtils.putString(application, LoginActivity.class.getName(), value);

                        view.showMessage(0, body.getMessage());
                    }

                    application.getAppComponent().extras().put(LoginActivity.class.getName(), user);
                    view.finishActivity();
                }
            }

            @Override
            protected void onFailure(ApiException exception) {
                view.showMessage(0, exception.getMessage());
            }

            @Override
            protected void onFinish(boolean isCanceled) {
                view.hideLoading();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (user != null) {
            user.cancel();
        }
    }
}