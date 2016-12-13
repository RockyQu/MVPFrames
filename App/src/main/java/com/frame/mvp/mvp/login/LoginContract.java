package com.frame.mvp.mvp.login;

import com.tool.common.frame.BaseModel;
import com.tool.common.frame.BasePresenter;
import com.tool.common.frame.BaseView;

/**
 * 定义Model、View的接口
 */
public interface LoginContract {

    interface Model extends BaseModel {

    }

    interface View extends BaseView {

        void loginSuccess();
    }

    abstract class Presenter extends BasePresenter<Model, View> {

        public abstract void login(String name, String password);
    }
}