package com.frame.mvp.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.frame.mvp.R;
import com.frame.mvp.ui.common.CommonActivity;
import com.frame.mvp.ui.widget.ToastBar;
import com.tool.common.utils.StringUtils;

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

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (inputCheck(R.id.btn_submit)) {
                    // 登录
                    presenter.login(edtAccount.getText().toString().trim(), edtPassword.getText().toString().trim());
                }

            }
        });
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
                    ToastBar.show(this, "账号不能为空");
                    return false;
                }
                if (StringUtils.isEmpty(edtPassword.getText().toString().trim())) {
                    ToastBar.show(this, "密码不能为空");
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
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