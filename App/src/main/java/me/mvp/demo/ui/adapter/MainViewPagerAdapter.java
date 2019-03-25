package me.mvp.demo.ui.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import me.mvp.demo.mvp.main.fragment.demo.DemoFragment;
import me.mvp.demo.mvp.main.fragment.main.MainFragment;
import me.mvp.demo.mvp.main.fragment.user.UserFragment;

import java.util.ArrayList;

/**
 * 主页面 Fragment 适配器
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public MainViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragments.clear();
        fragments.add(MainFragment.create(0));
        fragments.add(DemoFragment.create(1));
        fragments.add(UserFragment.create(2));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}