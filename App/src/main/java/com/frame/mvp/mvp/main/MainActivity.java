package com.frame.mvp.mvp.main;

import android.os.Bundle;

import com.frame.mvp.R;
import com.frame.mvp.ui.adapter.MainViewPagerAdapter;
import com.logg.Logg;
import com.tool.common.base.simple.base.BaseSimpleActivity;
import com.tool.common.frame.simple.ISimpleView;
import com.tool.common.frame.simple.Message;
import com.tool.common.http.download.Downloader;
import com.tool.common.http.download.exception.DownloadException;
import com.tool.common.http.download.request.DownloadRequest;
import com.tool.common.http.download.DownloadSimpleListener;
import com.tool.common.widget.ToastBar;
import com.tool.common.widget.navigation.BottomNavigation;
import com.tool.common.widget.navigation.BottomNavigationAdapter;
import com.tool.common.widget.navigation.BottomNavigationViewPager;

import butterknife.BindView;

/**
 * 主页面
 */
public class MainActivity extends BaseSimpleActivity<MainPresenter> implements ISimpleView {

    // UI
    @BindView(R.id.view_pager)
    BottomNavigationViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigation bottomNavigation;

    // Adapter
    private MainViewPagerAdapter adapter;

    @Override
    public void create(Bundle savedInstanceState) {

        // 隐藏导航栏Items
        BottomNavigationAdapter navigationAdapter = new BottomNavigationAdapter(this, R.menu.menu_bottom_navigation);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation);

        // 隐藏导航栏标题
        bottomNavigation.setTitleState(BottomNavigation.TitleState.ALWAYS_HIDE);

        // 导航点击事件
        bottomNavigation.setOnTabSelectedListener(new BottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                viewPager.setCurrentItem(position, false);

                ToastBar.with(MainActivity.this).setMessage("Success!").show();

                return true;
            }
        });

        viewPager.setOffscreenPageLimit(3);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        Downloader.getInstance().start(new DownloadRequest("http://download.alicdn.com/wireless/taobao4android/latest/702757.apk"),
                new DownloadSimpleListener() {

                    @Override
                    public void onStart(long total) {
                        Logg.e(total);
                    }

                    @Override
                    public void onProgress(long progress, long total) {
                        int current = (int) (progress * 1.0f / total * 100);
                        Logg.e(progress + "/" + total);
                    }

                    @Override
                    public void onFailure(DownloadException exception) {
                        Logg.e(exception.getExceptionMessage());
                    }

                    @Override
                    public void onFinish() {
                        Logg.e("onFinish");
                    }
                }
        );
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

    }

    @Override
    public MainPresenter obtainPresenter() {
        return new MainPresenter(component);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}