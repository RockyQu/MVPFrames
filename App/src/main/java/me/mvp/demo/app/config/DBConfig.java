package me.mvp.demo.app.config;

import android.content.Context;

import androidx.room.RoomDatabase;
import me.logg.Logg;
import me.mvp.demo.app.AppConfiguration;
import me.mvp.demo.db.AppDatabase;
import me.mvp.frame.db.DatabaseConfig;
import me.mvp.frame.di.module.DBModule;
import me.mvp.frame.utils.AppUtils;
import me.mvp.frame.utils.ProjectUtils;

/**
 * 扩展自定义配置数据库参数
 */
public class DBConfig implements DBModule.DBConfiguration {

    @SuppressWarnings("unchecked")
    @Override
    public void configDB(Context context, DatabaseConfig.Builder builder) {
        builder
                .path(ProjectUtils.ROOT_PATH)
                .name(AppUtils.Companion.getAppChannel(context, AppConfiguration.CHANNEL) + ".db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .databaseClass(AppDatabase.class)
                .allowMainThreadQueries()
        ;
    }

    @Override
    public void createdDB(Context context, RoomDatabase database) {
        Logg.e(database.getOpenHelper().getDatabaseName());
    }
}