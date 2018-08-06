package me.mvp.demo.app.config;

import android.content.Context;

import me.mvp.demo.app.AppConfiguration;
import me.mvp.demo.db.AppDatabase;
import me.mvp.frame.db.DatabaseConfig;
import me.mvp.frame.di.module.DBModule;
import me.mvp.frame.utils.AppUtils;

/**
 * 扩展自定义配置数据库参数
 */
public class DBConfig implements DBModule.DBConfiguration {

    @SuppressWarnings("unchecked")
    @Override
    public void configDB(Context context, DatabaseConfig.Builder builder) {
        builder
                .name(AppUtils.getAppChannel(context, AppConfiguration.CHANNEL))
                .databaseClass(AppDatabase.class)
                .allowMainThreadQueries()
        ;
    }
}