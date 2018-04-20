package me.mvp.demo.db;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import me.mvp.demo.entity.DaoMaster;
import me.mvp.demo.entity.DaoSession;
import me.mvp.demo.entity.UserDao;
import me.mvp.frame.db.DBContextWrapper;
import me.mvp.frame.db.MigrationHelper;
import me.mvp.frame.utils.DeviceUtils;
import me.mvp.frame.utils.ProjectUtils;

/**
 * DBModule
 */
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

    private DaoSession daoSession = null;

    public DBModule() {

    }

    private final static class SingletonHolder {
        private final static DBModule INSTANCE = new DBModule();
    }

    public static DBModule getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Application application) {
        this.init(application, ProjectUtils.DB, ProjectUtils.PROJECT_NAME);
    }

    public void init(Application application, String dbPath, String dbName) {
        this.application = application;

        this.dbPath = dbPath;
        this.dbName = dbName;

        if (ENCRYPTED) {
            KEY = DeviceUtils.getIMEI(application);
        }

        DBOpenHelper helper;
        if (!TextUtils.isEmpty(dbPath)) {
            helper = new DBOpenHelper(new DBContextWrapper(application, dbPath), !TextUtils.isEmpty(dbName) ? dbName : "db", null);
        } else {
            helper = new DBOpenHelper(new DBContextWrapper(application), !TextUtils.isEmpty(dbName) ? dbName : "db", null);
        }

        DaoMaster daoMaster = new DaoMaster(ENCRYPTED ? helper.getEncryptedWritableDb(KEY) : helper.getWritableDb());
        daoSession = daoMaster.newSession();
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

    public DaoSession getDaoSession() {
        return daoSession;
    }
}