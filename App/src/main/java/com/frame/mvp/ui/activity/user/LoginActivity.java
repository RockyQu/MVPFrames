package com.frame.mvp.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.frame.mvp.R;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.utils.AppUtils;

/**
 * 登录页面
 */
public class LoginActivity extends CommonActivity {

    /**
     * 账号
     */
    private EditText edtAccount = null;
    /**
     * 密码
     */
    private EditText edtPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtAccount = (EditText) findViewById(R.id.edt_account);
        edtPassword = (EditText) findViewById(R.id.edt_password);

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                AppUtils.call(LoginActivity.this, "18505126172");
            }
        });
    }
}

