package me.mvp.frame.base;

import android.support.annotation.NonNull;

import me.mvp.frame.di.component.AppComponent;

/**
 * 框架要求框架中的每个 {@link android.app.Application} 都需要实现此类
 */
public interface App {

    @NonNull
    AppComponent getAppComponent();
}