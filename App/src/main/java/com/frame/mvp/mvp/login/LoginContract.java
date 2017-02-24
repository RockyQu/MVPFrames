package com.frame.mvp.mvp.login;

import com.frame.mvp.entity.User;
import com.tool.common.frame.BaseView;
import com.tool.common.frame.IModel;
import com.tool.common.http.ResponseEntity;

import retrofit2.Call;

/**
 * 定义Model、View的接口
 */
public interface LoginContract {

    interface Model extends IModel{
        Call<ResponseEntity<User>> login(String name, String password);
    }

    interface View extends BaseView {

    }
}