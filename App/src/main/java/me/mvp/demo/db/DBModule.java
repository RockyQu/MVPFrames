package me.mvp.demo.db;

import android.app.Application;
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


    }


}