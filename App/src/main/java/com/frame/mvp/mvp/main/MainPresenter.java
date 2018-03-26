package com.frame.mvp.mvp.main;

import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.simple.BaseSimplePresenter;

public class MainPresenter extends BaseSimplePresenter<MainRepository> {

    // AppComponent
    private AppComponent component;

    public MainPresenter(AppComponent component) {
        super(component.getRepositoryManager().createRepository(MainRepository.class));
        this.component = component;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}