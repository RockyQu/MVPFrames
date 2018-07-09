package me.mvp.frame.di.module;

import android.app.Application;
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

        void configDB(Context context, DatabaseConfig.Builder builder);

        DBConfiguration EMPTY = new DBConfiguration() {

            @Override
            public void configDB(Context context, DatabaseConfig.Builder builder) {

            }
        };
    }
}