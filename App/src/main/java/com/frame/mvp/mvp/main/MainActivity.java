package com.frame.mvp.mvp.main;

import android.os.Bundle;

import com.frame.mvp.R;
import com.frame.mvp.db.DB;
import com.frame.mvp.entity.User;
import com.frame.mvp.ui.common.CommonActivity;
import com.tool.common.log.QLog;

/**
 * 主页面
 */
public class MainActivity extends CommonActivity {

    @Override
    public void create(Bundle savedInstanceState) {
        User user = new User();
        user.setName("测试fg");
//        DB.getInstance().getDaoSession().getUserDao().insert(user);

        application.getAppComponent().getDaoSession().getUserDao().insert(user);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}