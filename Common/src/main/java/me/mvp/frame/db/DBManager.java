package me.mvp.frame.db;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.di.module.DBModule;

/**
 * 数据库配置
 * <p>
 * 使用 2017 Google IO 大会 Architecture Components 架构组件 Room
 * <p>
 * 请在外部 Module 添加自己的数据库并继承 {@link RoomDatabase} ，调用 {@link AppComponent#dbManager()#database()}
 * 方法获取实例，{@link RoomDatabase} 已经实现单例化，外部无需再处理
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
    DBModule.DBConfiguration dbConfiguration;

    @Inject
    public DBManager() {

    }

    @Inject
    void init() {
        database = databaseBuilder();

        if (dbConfiguration != null) {
            dbConfiguration.createdDB(application, database);
        }
    }

    public RoomDatabase database() {
        return database;
    }

    @SuppressWarnings("unchecked")
    private <T extends RoomDatabase> T databaseBuilder() {
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

        RoomDatabase.Builder<T> builder = Room.databaseBuilder(application, klass, config.getPath() + config.getName());
        if (config.isAllowMainThreadQueries()) {
            builder.allowMainThreadQueries();
        }

        if (config.isFallbackToDestructiveMigration()) {
            builder.fallbackToDestructiveMigration();
        }

        int[] startVersions = config.getFallbackToDestructiveMigrationFromStartVersions();
        if (startVersions != null && startVersions.length != 0) {
            builder.fallbackToDestructiveMigrationFrom(startVersions);
        }

        if (config.getJournalMode() != null) {
            builder.setJournalMode(config.getJournalMode());
        }

        if (config.isFallbackToDestructiveMigrationOnDowngrade()) {
            builder.fallbackToDestructiveMigrationOnDowngrade();
        }

        if (config.getExecutor() != null) {
            builder.setQueryExecutor(config.getExecutor());
        }

        if (config.getFactory() != null) {
            builder.openHelperFactory(config.getFactory());
        }

        List<RoomDatabase.Callback> callbacks = config.getCallbacks();
        if (callbacks != null) {
            for (RoomDatabase.Callback callback : callbacks) {
                builder.addCallback(callback);
            }
        }

        if (config.getMigrations() != null) {
            builder.addMigrations(config.getMigrations());
        }

        return builder.build();
    }
}