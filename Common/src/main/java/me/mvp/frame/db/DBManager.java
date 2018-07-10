package me.mvp.frame.db;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.logg.Logg;

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

    // RoomDatabase
    private RoomDatabase database = null;

    @Inject
    Application application;

    @Inject
    DatabaseConfig config;

    @Inject
    public DBManager() {

    }

    @Inject
    void init() {
        database = databaseBuilder();
    }

    public RoomDatabase database() {
        return database;
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

    /**
     * Use this method instead of {@link Room#databaseBuilder(Context, Class, String)} to create a database.
     *
     * @param klass The abstract class which is annotated with {@link Database} and extends
     *              {@link RoomDatabase}.
     * @param <T>   The type of the database class.
     * @return A {@code RoomDatabaseBuilder<T>} which you can use to create the database.
     */
    private <T extends RoomDatabase> T databaseBuilder(@NonNull Class<T> klass) {
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