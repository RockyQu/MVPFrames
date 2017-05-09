package com.frame.mvp.mvp.main;

import android.os.Bundle;

import com.frame.mvp.R;
import com.tool.common.base.BaseActivity;
import com.tool.common.widget.ToastBar;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    @Override
    public void create(Bundle savedInstanceState) {
        ToastBar.create(getWindow().getDecorView(),"测试").show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}