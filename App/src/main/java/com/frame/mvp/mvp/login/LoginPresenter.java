package com.frame.mvp.mvp.login;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.entity.User;
import com.tool.common.frame.BasePresenter;
import com.tool.common.http.ResponseCallback;
import com.tool.common.http.ResponseEntity;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * LoginPresenter
 */
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {

    // Application
    private MVPApplication application;

    // Login User
    private Call<ResponseEntity<User>> user;

    public LoginPresenter(LoginContract.Model model, LoginContract.View view, MVPApplication application) {
        super(model, view);
        this.application = application;
    }

    public void login(final String account, final String password) {
        view.showLoading();
        user = model.login(account, password);
        user.enqueue(new ResponseCallback<ResponseEntity<User>>(application) {

            @Override
            protected void onResponse(ResponseEntity<User> body) {

                User user = body.getData();
                if (user != null) {
                    user.setAccount(account);
                    user.setPassword(password);

                    String value = GsonUtils.getString(user);
                    if (value != null) {
                        PreferencesUtils.putString(MVPApplication.getContext(), LoginActivity.FLAG_USER, value);

                        view.showMessage(body.getMessage());
                    }

                    application.setUser(user);
                    view.finishActivity();
                }
            }

            @Override
            protected void onFailure(String message) {
                view.showMessage(message);
            }

            @Override
            protected void onFinish() {
                view.hideLoading();
            }
        });
    }
}