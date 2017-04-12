package com.frame.mvp.mvp.main;

import android.os.Bundle;

import com.frame.mvp.R;
import com.frame.mvp.entity.User;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.log.QLog;

import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends CommonActivity {

    @Override
    public void create(Bundle savedInstanceState) {
        User user = new User();
        user.setName("测试fGGGGGGGGG");
        application.getAppComponent().getDaoSession().getUserDao().insert(user);

        List<User>  str = application.getAppComponent().getDaoSession().getUserDao().loadAll();
        QLog.e("AAAA"+str.get(0).getName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}