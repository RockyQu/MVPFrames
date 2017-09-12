package com.frame.mvp.mvp.main.fragment.tab2;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.frame.mvp.R;
import com.tool.common.base.simple.base.BaseSimpleFragment;
import com.tool.common.frame.IPresenter;

/**
 *
 */
public class Tab2Fragment extends BaseSimpleFragment {

    /**
     * Create Fragment
     *
     * @return
     */
    public static Fragment create(int index) {
        Tab2Fragment fragment = new Tab2Fragment();
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
        return R.layout.fragment_tab2;
    }
}