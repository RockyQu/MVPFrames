package me.mvp.demo.mvp.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;

import me.mvp.demo.R;
import me.mvp.demo.ui.adapter.MainViewPagerAdapter;
import me.mvp.demo.ui.widget.navigation.BottomNavigation;
import me.mvp.demo.ui.widget.navigation.BottomNavigationAdapter;
import me.mvp.demo.ui.widget.navigation.BottomNavigationViewPager;
import me.mvp.frame.base.BaseActivity;
import me.mvp.frame.di.module.AppModule;
import me.mvp.frame.frame.IView;
import me.mvp.frame.frame.Message;
import me.mvp.frame.widget.Snacker;
import me.mvp.frame.widget.Toaster;

import butterknife.BindView;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity<MainPresenter> implements IView {

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
        bottomNavigation.setTitleState(BottomNavigation.TitleState.ALWAYS_HIDE);

        // 导航点击事件
        bottomNavigation.setOnTabSelectedListener(new BottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                viewPager.setCurrentItem(position, false);

                Snacker.with(MainActivity.this).setMessage("警告").warning().show();

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

//        RxPermissions rxPermissions = new RxPermissions(this);
//        PermissionUtils.location(new PermissionUtils.RequestPermission() {
//
//            @Override
//            public void onRequestPermissionSuccess() {
//
//            }
//
//            @Override
//            public void onRequestPermissionFailure() {
//                // 如果失败跳到到应用设置页面
//                AppUtils.applicationDetailsSettings(MainActivity.this);
//            }
//        }, rxPermissions);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(int type, @NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

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