package me.mvp.demo.mvp.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;

import me.mvp.demo.R;
import me.mvp.demo.entity.User;
import me.mvp.demo.mvp.main.MainActivity;
import me.mvp.frame.base.BaseActivity;
import me.mvp.frame.frame.IView;
import me.mvp.frame.frame.Message;

import butterknife.BindView;
import butterknife.OnClick;
import me.mvp.frame.utils.StringUtils;
import me.mvp.frame.widget.Toaster;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements IView {

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

        }

        edtAccount.setText("18012345678");
        edtPassword.setText("123456");
    }

    @OnClick(R.id.btn_submit)
    public void login() {
        if (inputCheck(R.id.btn_submit)) {

            // 登录
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
//                // 手机号码
                if (StringUtils.isEmpty(edtAccount.getText().toString().trim())) {
//                    ToastBar.create(this, "输入手机号码").show();
//                    return false;
                }
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