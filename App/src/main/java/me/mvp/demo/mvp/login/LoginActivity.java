package me.mvp.demo.mvp.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import me.mvp.demo.R;
import me.mvp.demo.entity.User;
import me.mvp.frame.base.BaseActivity;
import me.mvp.frame.frame.IView;
import me.mvp.frame.frame.Message;

import me.mvp.frame.utils.StringUtils;
import me.mvp.frame.widget.Toaster;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements IView {

    @BindView(R.id.edt_account)
    AppCompatEditText edtAccount;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;
    @BindView(R.id.btn_submit)
    AppCompatButton btnSubmit;

    // 当前登录用户信息
    private User user;

    @SuppressLint("CheckResult")
    @Override
    public void create(Bundle savedInstanceState) {
        user = (User) component.extras().get(LoginActivity.class.getName());
        if (user != null) {

        }

        edtAccount.setText("18012345678");
        edtPassword.setText("123456");

        // 登录
        RxView.clicks(btnSubmit)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object v) throws Exception {
                        LoginActivity.this.login();
                    }
                });
    }

    /**
     * 登录
     */
    public void login() {
        if (inputCheck(R.id.btn_submit)) {
            presenter.login(Message.obtain(this), edtAccount.getText().toString().trim(), edtPassword.getText().toString().trim());
        }
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

                if (StringUtils.isEmpty(edtAccount.getText().toString().trim())) {
                    Toaster.with(this).setMessage("输入手机号码").show();
                    return false;
                }

                if (StringUtils.isEmpty(edtPassword.getText().toString().trim())) {
                    Toaster.with(this).setMessage("输入密码").show();
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
        setViewStatus(btnSubmit, false, "加载中");
    }

    @Override
    public void hideLoading() {
        setViewStatus(btnSubmit, true, "加载完成");
    }

    @Override
    public void showMessage(int type, @NonNull String message) {
        Toaster.with(this).setMessage(message).show();
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @Override
    public LoginPresenter obtainPresenter() {
        return new LoginPresenter(component);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
}