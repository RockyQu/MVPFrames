package me.mvp.demo.mvp.main.fragment.tab4;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import me.mvp.demo.R;
import me.mvp.frame.base.simple.base.BaseSimpleFragment;
import me.mvp.frame.frame.IPresenter;

/**
 *
 */
public class Tab4Fragment extends BaseSimpleFragment {

    /**
     * Create Fragment
     *
     * @return
     */
    public static Fragment create(int index) {
        Tab4Fragment fragment = new Tab4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void create(Bundle savedInstanceState) {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab4;
    }
}