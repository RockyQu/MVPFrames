package com.frame.mvp.mvp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.frame.mvp.R;
import com.frame.mvp.mvp.login.LoginActivity;
import com.frame.mvp.ui.common.CommonActivity;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends CommonActivity {

    @Override
    public void create(Bundle savedInstanceState) {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CHANGE_WIFI_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //...     
        } else {
            //...
        }
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}