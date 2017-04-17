package com.frame.mvp.mvp.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.frame.mvp.R;
import com.frame.mvp.entity.User;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.log.QLog;
import com.tool.common.widget.ToastBar;

import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends CommonActivity {

    @Override
    public void create(Bundle savedInstanceState) {
        ToastBar.create(getWindow().getDecorView(),"测试").show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}