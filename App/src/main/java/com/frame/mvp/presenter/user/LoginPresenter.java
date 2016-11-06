package com.frame.mvp.presenter.user;

import com.frame.mvp.contract.Contract;
import com.tool.common.frame.BasePresenter;

/**
 * LoginPresenter
 */
public class LoginPresenter extends BasePresenter<Contract.Model, Contract.View> {


    public LoginPresenter(Contract.View view) {
        super(view);
    }

    public LoginPresenter(Contract.Model model, Contract.View view) {
        super(model, view);


    }
}
