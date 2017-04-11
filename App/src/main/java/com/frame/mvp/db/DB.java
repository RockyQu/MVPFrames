package com.frame.mvp.db;

import com.frame.mvp.entity.DaoMaster;
import com.frame.mvp.entity.DaoSession;
import com.tool.common.db.DBContextWrapper;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by SZLD-PC-249 on 2017/4/11.
 */
public class DB {

    private static volatile DB manager;
    private static DaoMaster.DevOpenHelper helper;
    private static DaoSession daoSession;

    private static DaoMaster daoMaster;

    private DB() {

    }
    public static DB getInstance() {
        if (manager == null) {
            synchronized (DB.class) {
                if (manager == null) {
                    manager = new DB();
                }
            }
        }
        return manager;
    }


    //判断是否存在数据库，没有就创建
    public DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            helper = new DaoMaster.DevOpenHelper(new DBContextWrapper(), "ccccccccc.db", null);
            daoMaster = new DaoMaster(helper.getWritableDb());
        }
        return daoMaster;
    }
    //   数据处理
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
    //输出日志
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }
    public void close() {
        closeHelper();
        closeSession();
    }
    public void closeHelper() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }
    //关闭session
    public void closeSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

}
