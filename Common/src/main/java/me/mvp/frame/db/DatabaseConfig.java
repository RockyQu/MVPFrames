package me.mvp.frame.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * https://developer.android.google.cn/jetpack/androidx/releases/room
 *
 * @param <T>
 */
public class DatabaseConfig<T extends RoomDatabase> {

    private String name;
    private String path;

    private Class<T> databaseClass;

    private boolean allowMainThreadQueries;

    private boolean fallbackToDestructiveMigration;
    private int[] fallbackToDestructiveMigrationFromStartVersions;

    private RoomDatabase.JournalMode journalMode;

    private boolean fallbackToDestructiveMigrationOnDowngrade;

    private Executor executor;

    private SupportSQLiteOpenHelper.Factory factory;

    private List<RoomDatabase.Callback> callbacks;

    private Migration[] migrations;

    public DatabaseConfig(Builder<T> builder) {
        this.name = builder.name;
        this.path = builder.path;
        this.databaseClass = builder.databaseClass;
        this.allowMainThreadQueries = builder.allowMainThreadQueries;
        this.fallbackToDestructiveMigration = builder.fallbackToDestructiveMigration;
        this.fallbackToDestructiveMigrationFromStartVersions = builder.fallbackToDestructiveMigrationFromStartVersions;
        this.journalMode = builder.journalMode;
        this.fallbackToDestructiveMigrationOnDowngrade = builder.fallbackToDestructiveMigrationOnDowngrade;
        this.executor = builder.executor;
        this.factory = builder.factory;
        this.callbacks = builder.callbacks;
        this.migrations = builder.migrations;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Class<T> getDatabaseClass() {
        return databaseClass;
    }

    public boolean isAllowMainThreadQueries() {
        return allowMainThreadQueries;
    }

    public boolean isFallbackToDestructiveMigration() {
        return fallbackToDestructiveMigration;
    }

    public int[] getFallbackToDestructiveMigrationFromStartVersions() {
        return fallbackToDestructiveMigrationFromStartVersions;
    }

    public RoomDatabase.JournalMode getJournalMode() {
        return journalMode;
    }

    public boolean isFallbackToDestructiveMigrationOnDowngrade() {
        return fallbackToDestructiveMigrationOnDowngrade;
    }

    public Executor getExecutor() {
        return executor;
    }

    public SupportSQLiteOpenHelper.Factory getFactory() {
        return factory;
    }

    public List<RoomDatabase.Callback> getCallbacks() {
        return callbacks;
    }

    public Migration[] getMigrations() {
        return migrations;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T extends RoomDatabase> {

        // 名称
        private String name = "";
        // 路径
        private String path = "";

        // 提供给外部模块配置自己的数据库，请传入继承自 RoomDatabase 的类
        private Class<T> databaseClass;

        // 是否允许在主线程访问数据库
        private boolean allowMainThreadQueries = false;

        // 当升级数据库时，允许数据库抛弃旧数据重新创建新的数据库表
        private boolean fallbackToDestructiveMigration = false;

        // 通知数据库，允许从特定的版本中抛弃旧数据重新创建新的数据库表
        private int[] fallbackToDestructiveMigrationFromStartVersions;

        // 设置数据库的日志模式
        private RoomDatabase.JournalMode journalMode;

        // 如果发生降级，是否会自动重新创建数据库
        private boolean fallbackToDestructiveMigrationOnDowngrade = false;

        // 设置自定义线程池
        private Executor executor;

        // 设置数据库工厂
        private SupportSQLiteOpenHelper.Factory factory;

        // 数据库状态监听
        private List<RoomDatabase.Callback> callbacks;

        // 版本迁移构建器
        // 每个迁移都有一个开始和结束版本，Room运行这些迁移以将数据库带到最新版本。
        // 如果当前版本和最新版本之间缺少迁移项目，Room将清除数据库并重新创建，即使两个版本之间没有更改，您仍应向构建器提供迁移对象。
        // 迁移可以处理多个版本（例如，如果在没有转到版本4的情况下，在执行版本3到5时有更快的路径可供选择）。如果Room打开版本3的数据库且最新版本> = 5，则Room将使用可以从3迁移到5而不是3到4和4到5的迁移对象。
        private Migration[] migrations;

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> path(String path) {
            this.path = path;
            return this;
        }

        public Builder<T> databaseClass(Class<T> databaseClass) {
            this.databaseClass = databaseClass;
            return this;
        }

        public Builder<T> allowMainThreadQueries() {
            this.allowMainThreadQueries = true;
            return this;
        }

        public Builder<T> fallbackToDestructiveMigration() {
            this.fallbackToDestructiveMigration = true;
            return this;
        }

        public Builder<T> fallbackToDestructiveMigrationFromStartVersions(int[] startVersions) {
            this.fallbackToDestructiveMigrationFromStartVersions = startVersions;
            return this;
        }

        public Builder<T> setJournalMode(RoomDatabase.JournalMode journalMode) {
            this.journalMode = journalMode;
            return this;
        }

        public Builder<T> fallbackToDestructiveMigrationOnDowngrade() {
            this.fallbackToDestructiveMigrationOnDowngrade = true;
            return this;
        }

        public Builder<T> setQueryExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder<T> openHelperFactory(SupportSQLiteOpenHelper.Factory factory) {
            this.factory = factory;
            return this;
        }

        public Builder<T> addCallback(RoomDatabase.Callback callback) {
            if (callbacks == null) {
                callbacks = new ArrayList<>();
            }
            callbacks.add(callback);
            return this;
        }

        public Builder<T> addCallback(Migration... migrations) {
            this.migrations = migrations;
            return this;
        }


        public DatabaseConfig build() {
            return new DatabaseConfig(this);
        }
    }
}