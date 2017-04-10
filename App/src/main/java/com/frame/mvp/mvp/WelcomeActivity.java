package com.frame.mvp.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.frame.mvp.R;
import com.frame.mvp.mvp.login.LoginActivity;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.utils.PermissionUtils;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 欢迎页面
 */
public class WelcomeActivity extends CommonActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    public void create(Bundle savedInstanceState) {
        // 权限处理
        if (EasyPermissions.hasPermissions(this, PermissionUtils.PERMISSIONS)) {
            this.startNextActivity();
        } else {
            // Ask for dangerous permission.
            EasyPermissions.requestPermissions(this, getString(R.string.PermissionsMessage),
                    PermissionUtils.PERMISSION_CODE, PermissionUtils.PERMISSIONS);
        }
    }

    /**
     * 跳转下一页面
     */
    private void startNextActivity() {
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms.size() == PermissionUtils.PERMISSIONS.length) {
            this.startNextActivity();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle(R.string.PermissionsErrorTitle)
                    .setRationale(R.string.PermissionsErrorMessage)
                    .build()
                    .show();
        }
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