package com.frame.mvp.mvp.main.fragment.tab1.network;

import android.os.Bundle;

import com.frame.mvp.R;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.main.MainPresenter;
import com.frame.mvp.ui.adapter.MainViewPagerAdapter;
import com.tool.common.base.simple.base.BaseSimpleActivity;
import com.tool.common.frame.simple.ISimpleView;
import com.tool.common.frame.simple.Message;
import com.tool.common.widget.navigation.BottomNavigation;
import com.tool.common.widget.navigation.BottomNavigationAdapter;
import com.tool.common.widget.navigation.BottomNavigationViewPager;

import butterknife.BindView;

/**
 * Network example
 */
public class NetworkActivity extends BaseSimpleActivity<NetworkPresenter> implements ISimpleView {

    @Override
    public void create(Bundle savedInstanceState) {

        presenter.login(Message.obtain(this), "账号", "密码");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(int type, String message) {

    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case NetworkPresenter.FLAG_LOGIN:
                User user = (User) message.obj;
                if (user != null) {

                }
                break;
            default:
                break;
        }
    }

    @Override
    public NetworkPresenter obtainPresenter() {
        return new NetworkPresenter(component);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}