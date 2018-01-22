package com.frame.mvp.mvp.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.frame.mvp.R;
import com.frame.mvp.ui.adapter.MainViewPagerAdapter;
import com.logg.Logg;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tool.common.base.simple.base.BaseSimpleActivity;
import com.tool.common.frame.simple.ISimpleView;
import com.tool.common.frame.simple.Message;
import com.tool.common.http.download.Downloader;
import com.tool.common.http.download.DownloaderSampleListener;
import com.tool.common.http.download.exception.DownloadException;
import com.tool.common.utils.AppUtils;
import com.tool.common.utils.PermissionUtils;
import com.tool.common.widget.Snacker;
import com.tool.common.widget.Toaster;
import com.tool.common.widget.navigation.BottomNavigation;
import com.tool.common.widget.navigation.BottomNavigationAdapter;
import com.tool.common.widget.navigation.BottomNavigationViewPager;


import butterknife.BindView;
import io.reactivex.functions.Consumer;

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

        bottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.main_color));

        // 隐藏导航栏标题
        bottomNavigation.setTitleState(BottomNavigation.TitleState.ALWAYS_SHOW);

        // 导航点击事件
        bottomNavigation.setOnTabSelectedListener(new BottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                viewPager.setCurrentItem(position, false);

                Snacker.with(MainActivity.this).setMessage("Success").warning().show();

                Toaster.with(MainActivity.this).setMessage("上传成功").show();
                return true;
            }
        });

        viewPager.setOffscreenPageLimit(3);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

//        Downloader.getInstance().create("http://download.alicdn.com/wireless/taobao4android/latest/702757.apk")
////                .setSaveFilePath(ProjectUtils.OTHER + "aaaaa")
//                .setListener(new DownloaderSampleListener() {
//
//                    @Override
//                    protected void onConnection(boolean isContinue, long progress, long total) {
//                        Logg.e(progress + "/" + total);
//                    }
//
//                    @Override
//                    protected void onProgress(long progress, long total, int speed) {
//                        int current = (int) (progress * 1.0f / total * 100);
//                        Logg.e(progress + "/" + total);
//                    }
//
//                    @Override
//                    protected void onFailure(DownloadException exception) {
//                        exception.printStackTrace();
//                    }
//
//                    @Override
//                    protected void onPaused() {
//                        Logg.e("onPaused");
//                    }
//
//                    @Override
//                    protected void onComplete(String filePath) {
//                        Logg.e("onFinish " + filePath);
//                    }
//                })
//                .start();

        RxPermissions rxPermissions = new RxPermissions(this);
        PermissionUtils.location(new PermissionUtils.RequestPermission() {

            @Override
            public void onRequestPermissionSuccess() {

            }

            @Override
            public void onRequestPermissionFailure() {
                // 如果失败跳到到应用设置页面
                AppUtils.applicationDetailsSettings(MainActivity.this);
            }
        }, rxPermissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static final int REQUEST_LOCATION_PERMISSION = 1;

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