package me.mvp.frame.di.module;

import android.app.Application;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.logg.Logg;
import me.mvp.frame.db.DatabaseConfig;

/**
 * 数据库配置
 */
@Module
public class DBModule {

    @Singleton
    @Provides
    DatabaseConfig provideDatabaseConfig(Application application, DBModule.DBConfiguration dbConfiguration, DatabaseConfig.Builder builder) {
        if (dbConfiguration != null) {
            dbConfiguration.configDB(application, builder);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    DatabaseConfig.Builder provideDatabaseConfigBuilder() {
        return new DatabaseConfig.Builder();
    }

    /**
     * 提供一个数据库配置接口，用于对数据库进行格外的参数配置
     */
    public interface DBConfiguration {

        /**
         * 在数据库被创建之前的一些配置
         */
        void configDB(Context context, DatabaseConfig.Builder builder);

        /**
         * 在数据库创建成功之后返回数据库实例，你可以再次进行一些格外的配置
         */
        void createdDB(Context context, RoomDatabase database);

        DBConfiguration EMPTY = new DBConfiguration() {

            @Override
            public void configDB(Context context, DatabaseConfig.Builder builder) {

            }

            @Override
            public void createdDB(Context context, RoomDatabase database) {

            }
        };
    }
}