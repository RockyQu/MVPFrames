package me.mvp.demo.app.config;

import android.content.Context;

import me.mvp.demo.db.AppDatabase;
import me.mvp.frame.db.DatabaseConfig;
import me.mvp.frame.di.module.DBModule;

/**
 * 扩展自定义配置数据库参数
 */
public class DBConfig implements DBModule.DBConfiguration {

    @SuppressWarnings("unchecked")
    @Override
    public void configDB(Context context, DatabaseConfig.Builder builder) {
        builder
                .name("Frames")
                .databaseClass(AppDatabase.class)
                .allowMainThreadQueries()
        ;
    }
}