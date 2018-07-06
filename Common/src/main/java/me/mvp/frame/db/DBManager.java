package me.mvp.frame.db;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 数据库配置
 * <p>
 * 使用 2017 Google IO 大会 Architecture Components 架构组件 Room
 *
 * @see <a href="https://developer.android.google.cn/training/data-storage/room"></a>
 * @see <a href="https://github.com/googlesamples/android-architecture-components"></a>
 */
@Singleton
public class DBManager {

    @Inject
    Application application;

    @Inject
    DatabaseConfig config;

    @Inject
    public DBManager() {

    }

    @SuppressWarnings("unchecked")
    public <T extends RoomDatabase> T databaseBuilder() {
        if (config.getDatabaseClass() == null) {
            throw new IllegalArgumentException("Cannot build a database with null or empty name."
                    + " If you are trying to create an in memory database, use Room"
                    + ".inMemoryDatabaseBuilder");
        }
        return (T) databaseBuilder(config.getDatabaseClass());
    }

    public <T extends RoomDatabase> T databaseBuilder(@NonNull Class<T> klass) {
        if (TextUtils.isEmpty(config.getName())) {
            throw new IllegalArgumentException("Cannot build a database with null or empty name."
                    + " If you are trying to create an in memory database, use Room"
                    + ".inMemoryDatabaseBuilder");
        }

        RoomDatabase.Builder<T> builder = Room.databaseBuilder(application, klass, config.getName());
        if (config.isAllowMainThreadQueries()) {
            builder.allowMainThreadQueries();
        }

        return builder.build();
    }
}