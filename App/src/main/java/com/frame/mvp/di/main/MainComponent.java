package com.frame.mvp.di.main;

import com.frame.mvp.mvp.main.MainActivity;
import com.tool.common.di.component.AppComponent;
import com.tool.common.di.scope.ActivityScope;

import dagger.Component;

/**
 * MainComponent
 */
@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {

    void inject(MainActivity activity);
}