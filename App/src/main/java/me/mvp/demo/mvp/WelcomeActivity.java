package me.mvp.demo.mvp;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import me.mvp.demo.R;
import me.mvp.demo.app.AppConfiguration;
import me.mvp.demo.mvp.login.LoginActivity;
import me.mvp.demo.mvp.main.MainActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import me.mvp.frame.base.BaseActivity;
import me.mvp.frame.frame.IPresenter;
import me.mvp.frame.utils.AppUtils;
import me.mvp.frame.utils.PermissionUtils;
import me.mvp.frame.utils.ProjectUtils;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity {

    private RxPermissions rxPermissions;

    @Override
    public void create(Bundle savedInstanceState) {
        rxPermissions = new RxPermissions(this);
        PermissionUtils.requestPermissions(new PermissionUtils.RequestPermission() {

                                               @Override
                                               public void onRequestPermissionSuccess() {
                                                   // 创建一些文件夹
                                                   ProjectUtils.init(AppUtils.getAppChannel(WelcomeActivity.this, AppConfiguration.CHANNEL));

                                                   startNextActivity();
                                               }

                                               @Override
                                               public void onRequestPermissionFailure() {
                                                   // 如果失败跳到到应用设置页面
                                                   AppUtils.applicationDetailsSettings(WelcomeActivity.this);
                                                   finish();
                                               }
                                           }, rxPermissions,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 跳转下一页面
     */
    private void startNextActivity() {
        new Handler(msg -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.finish();
            return false;
        }).sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxPermissions = null;
    }
}