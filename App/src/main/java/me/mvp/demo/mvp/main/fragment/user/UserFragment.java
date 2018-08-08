package me.mvp.demo.mvp.main.fragment.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import me.logg.Logg;
import me.mvp.demo.R;
import me.mvp.frame.base.BaseFragment;
import me.mvp.frame.frame.IPresenter;

/**
 * User
 */
public class UserFragment extends BaseFragment {

    /**
     * Create Fragment
     *
     * @return
     */
    public static Fragment create(int index) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        Logg.e("UserFragment");
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab3;
    }
}