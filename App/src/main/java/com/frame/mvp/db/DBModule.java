package com.frame.mvp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.frame.mvp.app.MVPApplication;
import com.frame.mvp.entity.DaoMaster;
import com.frame.mvp.entity.DaoSession;
import com.frame.mvp.entity.UserDao;
import com.tool.common.db.DBContextWrapper;
import com.tool.common.db.MigrationHelper;
import com.tool.common.di.scope.ApplicationScope;
import com.tool.common.utils.DeviceUtils;
import com.tool.common.utils.ProjectUtils;

import dagger.Module;
import dagger.Provides;

/**
 * DBModule
 */
@Module
public class DBModule {

    // 是否加密
    public static final boolean ENCRYPTED = false;
    // 加密密钥，客户端可以自己生成一个特征性动态Key,也可服务器配合传过来一个Key
    public static String KEY;

    public DBModule() {
        if (ENCRYPTED) {
            KEY = DeviceUtils.getIMEI(MVPApplication.getContext());
        }
    }

    @ApplicationScope
    @Provides
    public DBOpenHelper provideDevOpenHelper() {
        return new DBOpenHelper(new DBContextWrapper(), ProjectUtils.PROJECT_NAME + ".db", null);
    }

    @ApplicationScope
    @Provides
    public DaoMaster provideDaoMaster(DBOpenHelper helper) {
        return new DaoMaster(ENCRYPTED ? helper.getEncryptedWritableDb(KEY) : helper.getWritableDb());
    }

    @ApplicationScope
    @Provides
    public DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    /**
     * 数据库升级模块
     */
    public class DBOpenHelper extends DaoMaster.OpenHelper {

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            MigrationHelper.getInstance().migrate(db, UserDao.class);
        }
    }

//    //输出日志
//    public void setDebug() {
//        QueryBuilder.LOG_SQL = true;
//        QueryBuilder.LOG_VALUES = true;
//    }
//
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