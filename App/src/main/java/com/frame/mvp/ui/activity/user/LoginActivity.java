package com.frame.mvp.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.frame.mvp.R;
import com.frame.mvp.contract.Contract;
import com.frame.mvp.presenter.user.LoginPresenter;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.utils.AppUtils;

/**
 * 登录页面
 */
public class LoginActivity extends CommonActivity<LoginPresenter> implements Contract.View {

    // 账号
    private EditText edtAccount = null;
    // 密码
    private EditText edtPassword = null;

    @Override
    public void create(Bundle savedInstanceState) {

//        presenter =

        edtAccount = (EditText) findViewById(R.id.edt_account);
        edtPassword = (EditText) findViewById(R.id.edt_password);

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}