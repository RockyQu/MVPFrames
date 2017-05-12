package com.frame.mvp.mvp.main;

import android.content.Intent;
import android.os.Bundle;

import com.frame.mvp.R;
import com.frame.mvp.di.main.DaggerMainComponent;
import com.frame.mvp.di.main.MainModule;
import com.tool.common.base.BaseActivity;
import com.tool.common.di.component.AppComponent;
import com.tool.common.log.QLog;
import com.tool.common.widget.ToastBar;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @Override
    public void create(Bundle savedInstanceState) {
        presenter.onDestroy();
        ToastBar.create(getWindow().getDecorView(), "测试").show();
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
    public void launchActivity(Intent intent) {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void setupActivityComponent(AppComponent component) {
        DaggerMainComponent
                .builder()
                .appComponent(component)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}