package com.frame.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.frame.mvp.mvp.main.fragment.tab1.Tab1Fragment;
import com.frame.mvp.mvp.main.fragment.tab2.Tab2Fragment;
import com.frame.mvp.mvp.main.fragment.tab3.Tab3Fragment;
import com.frame.mvp.mvp.main.fragment.tab4.Tab4Fragment;

import java.util.ArrayList;

/**
 *
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public MainViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragments.clear();
        fragments.add(Tab1Fragment.create(0));
        fragments.add(Tab2Fragment.create(1));
        fragments.add(Tab3Fragment.create(2));
        fragments.add(Tab4Fragment.create(3));
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