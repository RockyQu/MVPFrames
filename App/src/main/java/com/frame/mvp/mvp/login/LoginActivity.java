package com.frame.mvp.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.frame.mvp.R;
import com.frame.mvp.app.api.service.ServiceManager;
import com.frame.mvp.di.AppComponent;
import com.frame.mvp.entity.User;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.utils.StringUtils;
import com.tool.common.widget.ToastBar;

import butterknife.BindView;

/**
 * 登录页面
 */
public class LoginActivity extends CommonActivity<LoginPresenter> implements LoginContract.View, OnClickListener {

    // 账号
    @BindView(R.id.edt_account)
    EditText edtAccount;
    // 密码
    @BindView(R.id.edt_password)
    EditText edtPassword;

    // 提交
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    // 当前登录用户信息
    private User user;

    public static final String FLAG_USER = "user";
    public static final String FLAG_LOGIN = "user_login";

    @Override
    public void create(Bundle savedInstanceState) {

        user = application.getUser();
        if (user != null) {
            edtAccount.setText(user.getAccount());
            edtPassword.setText(user.getPassword());
        }

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (inputCheck(R.id.btn_submit)) {
                    // 登录
                    presenter.login(edtAccount.getText().toString().trim(), edtPassword.getText().toString().trim());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected LoginPresenter setupActivityComponent(AppComponent appComponent) {
        return null;
//        return new LoginPresenter(application, new LoginModel(ServiceManager.getInstance(application.getHttpModule().retrofit)), this);
    }

    /**
     * 输入项合法性检查
     *
     * @param flag Flag
     * @return
     */
    private boolean inputCheck(int flag) {
        switch (flag) {
            case R.id.btn_submit:// 登录
                // 手机号码
                if (StringUtils.isEmpty(edtAccount.getText().toString().trim())) {
                    ToastBar.show(this, "输入手机号码");
                    return false;
                }
                // 手机号码
                if (StringUtils.isPhone(edtAccount.getText().toString().trim())) {
                    ToastBar.show(this, "手机号码不正确");
                    return false;
                }
                // 密码
                if (StringUtils.isEmpty(edtPassword.getText().toString().trim())) {
                    ToastBar.show(this, "输入密码");
                    return false;
                }
                // 密码位数
                if (StringUtils.isCount(edtPassword.getText().toString().trim(), 6, 20)) {
                    ToastBar.show(this, "密码为6-20位");
                    return false;
                }

                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 设置View状态
     *
     * @param view    View
     * @param enabled 显示状态
     * @param message 文字说明
     */
    private void setViewStatus(Button view, boolean enabled, String message) {
        view.setEnabled(enabled);
        view.setText(message);
    }

    @Override
    public void showLoading() {
        setViewStatus(btnSubmit, false, "登录中");
    }

    @Override
    public void hideLoading() {
        setViewStatus(btnSubmit, true, "登   录");
    }

    @Override
    public void showMessage(String message) {
        ToastBar.show(LoginActivity.this, message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        if (application.getUser() != null) {
            intent.putExtra(FLAG_LOGIN, true);
        } else {
            intent.putExtra(FLAG_LOGIN, false);
        }
        setResult(0, intent);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onBackPressed() {
        finishActivity();
        super.onBackPressed();
    }
}