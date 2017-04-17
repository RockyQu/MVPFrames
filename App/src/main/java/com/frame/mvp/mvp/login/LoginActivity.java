package com.frame.mvp.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.frame.mvp.R;
import com.frame.mvp.di.common.component.AppComponent;
import com.frame.mvp.di.di.login.DaggerLoginComponent;
import com.frame.mvp.di.di.login.LoginModule;
import com.frame.mvp.entity.User;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.utils.StringUtils;
import com.tool.common.widget.ToastBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends CommonActivity<LoginPresenter> implements LoginContract.View {

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
    }

    @OnClick(R.id.btn_submit)
    public void login() {
        if (inputCheck(R.id.btn_submit)) {
            // 登录
            presenter.login(edtAccount.getText().toString().trim(), edtPassword.getText().toString().trim());
        }
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
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
                    ToastBar.create(this, "输入手机号码").show();
                    return false;
                }
                // 手机号码
                if (StringUtils.isPhone(edtAccount.getText().toString().trim())) {
                    ToastBar.create(this, "手机号码不正确").show();
                    return false;
                }
                // 密码
                if (StringUtils.isEmpty(edtPassword.getText().toString().trim())) {
                    ToastBar.create(this, "输入密码").show();
                    return false;
                }
                // 密码位数
                if (StringUtils.isCount(edtPassword.getText().toString().trim(), 6, 20)) {
                    ToastBar.create(this, "密码为6-20位").show();
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
        ToastBar.create(LoginActivity.this, message).show();
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