package com.frame.mvp.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.frame.mvp.db.DBModule;
import com.frame.mvp.db.di.DBComponent;
import com.frame.mvp.db.di.DaggerDBComponent;
import com.tool.common.base.BaseApplication;

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