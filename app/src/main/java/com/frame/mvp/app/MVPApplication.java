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

    // DBComponent
    private DBComponent component = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // DBComponent
        component = DaggerDBComponent
                .builder()
                .dBModule(new DBModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 返回DBComponent提供数据库模块统一出口
     *
     * @return
     */
    public DBComponent getDBComponent() {
        return component;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}