package com.frame.mvp.db;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.frame.mvp.db.build.DaoMaster;
import com.frame.mvp.db.build.DaoSession;
import com.frame.mvp.db.build.UserDao;
import com.tool.common.db.DBContextWrapper;
import com.tool.common.db.MigrationHelper;
import com.tool.common.di.scope.ApplicationScope;
import com.tool.common.utils.DeviceUtils;
import com.tool.common.utils.ProjectUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import dagger.Module;
import dagger.Provides;

/**
 * DBModule
 */
@Module
public class DBModule {

    // Application
    private Application application;

    // DB存储路径
    private String dbPath;
    // DB名称
    private String dbName;

    // 是否加密
    public static final boolean ENCRYPTED = false;
    // 加密密钥，客户端可以自己生成一个特征性动态Key,也可服务器配合传过来一个Key
    public static String KEY;

    public DBModule(Application application) {
        this(application, ProjectUtils.DB, ProjectUtils.PROJECT_NAME);
    }

    public DBModule(Application application, String dbPath, String dbName) {
        this.application = application;

        this.dbPath = dbPath;
        this.dbName = dbName;

        if (ENCRYPTED) {
            KEY = DeviceUtils.getIMEI(application);
        }
    }

    @ApplicationScope
    @Provides
    public DBOpenHelper provideDevOpenHelper() {
        if (!TextUtils.isEmpty(dbPath)) {
            return new DBOpenHelper(new DBContextWrapper(application, dbPath), !TextUtils.isEmpty(dbName) ? dbName : "db", null);
        }
        return new DBOpenHelper(new DBContextWrapper(application), !TextUtils.isEmpty(dbName) ? dbName : "db", null);
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
}