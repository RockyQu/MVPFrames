package com.frame.mvp.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.frame.mvp.R;
import com.frame.mvp.ui.common.CommonActivity;

import butterknife.BindView;

/**
 * 登录页面
 */
public class LoginActivity extends CommonActivity<LoginPresenter> implements LoginContract.View {

    // 账号
    @BindView(R.id.edt_account)
    EditText edtAccount = null;
    // 密码
    @BindView(R.id.edt_password)
    EditText edtPassword = null;

    // 提交
    @BindView(R.id.btn_submit)
    Button btnSubmit = null;

    @Override
    public void create(Bundle savedInstanceState) {


//        presenter = new LoginPresenter(this);

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
    public void loginSuccess() {

    }

    @Override
    public void launchActivity(Intent intent) {

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