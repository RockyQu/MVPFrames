package com.frame.mvp.di.common.module;

import com.frame.mvp.entity.DaoMaster;
import com.frame.mvp.entity.DaoSession;
import com.tool.common.db.DBContextWrapper;
import com.tool.common.utils.ProjectUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DBModule
 */
@Module
public class DBModule {

    @Singleton
    @Provides
    public DaoMaster.DevOpenHelper provideDevOpenHelper() {
        return new DaoMaster.DevOpenHelper(new DBContextWrapper(), ProjectUtils.PROJECT_NAME + ".db", null);
    }

    @Singleton
    @Provides
    public DaoMaster provideDaoMaster(DaoMaster.DevOpenHelper devOpenHelper) {
        return new DaoMaster(devOpenHelper.getWritableDb());
    }

    @Singleton
    @Provides
    public DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    //输出日志
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

//    public void close() {
//        closeHelper();
//        closeSession();
//    }
//    public void closeHelper() {
//        if (helper != null) {
//            helper.close();
//            helper = null;
//        }
//    }
//    //关闭session
//    public void closeSession() {
//        if (daoSession != null) {
//            daoSession.clear();
//            daoSession = null;
//        }
//    }
}