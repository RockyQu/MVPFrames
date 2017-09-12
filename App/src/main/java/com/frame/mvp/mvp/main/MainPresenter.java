package com.frame.mvp.mvp.main;

import com.tool.common.di.component.AppComponent;
import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.simple.BaseSimplePresenter;

/**
 * MainPresenter
 */
@ActivityScope
public class MainPresenter extends BaseSimplePresenter<MainRepository> {

    // AppComponent
    private AppComponent component;

    public MainPresenter(AppComponent component) {
        super(component.getRepositoryManager().createRepository(MainRepository.class));
        this.component = component;
    }
}