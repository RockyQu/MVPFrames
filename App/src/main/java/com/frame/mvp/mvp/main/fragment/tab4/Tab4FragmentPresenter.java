package com.frame.mvp.mvp.main.fragment.tab4;

import com.logg.Logg;
import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.simple.BaseSimplePresenter;

/**
 * Presenter
 */
public class Tab4FragmentPresenter extends BaseSimplePresenter<Tab4FragmentRepository> {

    // AppComponent
    private AppComponent component;

    public Tab4FragmentPresenter(AppComponent component) {
        super(component.getRepositoryManager().createRepository(Tab4FragmentRepository.class));
        this.component = component;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}