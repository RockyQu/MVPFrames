package com.frame.mvp.db.di;

import android.app.Application;

import com.frame.mvp.db.DBModule;
import com.frame.mvp.entity.DaoSession;
import com.tool.common.di.scope.ApplicationScope;

import dagger.Component;

@ApplicationScope
@Component(modules = {DBModule.class})
public interface DBComponent {

    // DB
    DaoSession getDaoSession();

    void inject(Application application);
}