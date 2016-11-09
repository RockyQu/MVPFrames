package com.frame.mvp.contract.user;

import com.tool.common.frame.BaseView;

/**
 * 定义Model、View的接口
 */
public interface LoginContract {

    // 常用基本View方法定义到BaseView中
    interface View extends BaseView {

        void loginSuccess();
    }

    //
    interface Model {
        //TODO
    }
}