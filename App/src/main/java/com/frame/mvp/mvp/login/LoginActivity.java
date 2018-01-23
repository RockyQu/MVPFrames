package com.frame.mvp.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.frame.mvp.R;
import com.frame.mvp.di.login.DaggerLoginComponent;
import com.frame.mvp.di.login.LoginModule;
import com.frame.mvp.entity.User;
import com.tool.common.base.BaseActivity;
import com.tool.common.di.component.AppComponent;
import com.tool.common.widget.Snacker;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

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

    @Override
    public void create(Bundle savedInstanceState) {
        user = (User) component.extras().get(LoginActivity.class.getName());
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
//            presenter.test();
        }
    }

    @Override
    public void setupActivityComponent(AppComponent component) {
        DaggerLoginComponent
                .builder()
                .appComponent(component)
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
//                // 手机号码
//                if (StringUtils.isEmpty(edtAccount.getText().toString().trim())) {
//                    ToastBar.create(this, "输入手机号码").show();
//                    return false;
//                }
//                // 密码
//                if (StringUtils.isEmpty(edtPassword.getText().toString().trim())) {
//                    ToastBar.create(this, "输入密码").show();
//                    return false;
//                }

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
    public void showMessage(int type, String message) {
        Snacker.with(LoginActivity.this).setMessage(message).show();
    }

    @Override
    public void launchActivity(Intent intent) {
        setResult(0, intent);
        finish();
    }

    @Override
    public void finishActivity() {

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