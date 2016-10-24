package com.frame.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.frame.mvp.R;
import com.frame.mvp.ui.activity.user.LoginActivity;
import com.frame.mvp.ui.common.CommonActivity;

/**
 *
 */
public class WelcomeActivity extends CommonActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void create(Bundle savedInstanceState) {
//        new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
//                startActivity(intent);
//                return false;
//            }
//        }).sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}