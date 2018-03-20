package com.frame.mvp.mvp.login;

import com.frame.mvp.entity.User;
import com.tool.common.frame.IView;
import com.tool.common.frame.IModel;
import com.tool.common.http.ResponseEntity;

import retrofit2.Call;

public interface LoginContract {

    interface Model extends IModel{
        Call<ResponseEntity<User>> login(String name, String password);
    }

    interface View extends IView {

    }
}