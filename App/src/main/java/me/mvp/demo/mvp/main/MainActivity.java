package me.mvp.demo.mvp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import me.mvp.demo.R;
import me.mvp.demo.databinding.ActivityMainBinding;
import me.mvp.demo.ui.adapter.MainViewPagerAdapter;
import me.mvp.demo.ui.widget.navigation.BottomNavigation;
import me.mvp.demo.ui.widget.navigation.BottomNavigationAdapter;
import me.mvp.frame.base.BaseActivity;
import me.mvp.frame.frame.IView;
import me.mvp.frame.frame.Message;
import me.mvp.frame.widget.Snacker;
import me.mvp.frame.widget.Toaster;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> implements IView {

    // Adapter
    private MainViewPagerAdapter adapter;

    @Override
    public void create(Bundle savedInstanceState) {

        // 隐藏导航栏Items
        BottomNavigationAdapter navigationAdapter = new BottomNavigationAdapter(this, R.menu.menu_bottom_navigation);
        navigationAdapter.setupWithBottomNavigation(view.bottomNavigation);

        // 隐藏导航栏标题
        view.bottomNavigation.setTitleState(BottomNavigation.TitleState.ALWAYS_HIDE);

        // 导航点击事件
        view.bottomNavigation.setOnTabSelectedListener(new BottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                view.viewPager.setCurrentItem(position, false);

                Snacker.with(MainActivity.this).setMessage("警告").warning().show();

                Toaster.with(MainActivity.this).setMessage("上传成功").show();
                return true;
            }
        });

        view.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                view.bottomNavigation.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        view.viewPager.setOffscreenPageLimit(3);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        view.viewPager.setAdapter(adapter);
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