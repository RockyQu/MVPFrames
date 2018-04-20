package me.mvp.demo.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import me.mvp.frame.base.BaseApplication;

/**
 * Application
 */
public class MVPApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}