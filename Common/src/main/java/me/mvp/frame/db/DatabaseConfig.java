package me.mvp.frame.db;

import androidx.room.RoomDatabase;

public class DatabaseConfig<T extends RoomDatabase> {

    private String name;
    private String path;

    private Class<T> databaseClass;

    private boolean allowMainThreadQueries;

    private boolean fallbackToDestructiveMigration;
    private int[] fallbackToDestructiveMigrationFromStartVersions;

    private RoomDatabase.JournalMode journalMode;

    public DatabaseConfig(Builder<T> builder) {
        this.name = builder.name;
        this.path = builder.path;
        this.databaseClass = builder.databaseClass;
        this.allowMainThreadQueries = builder.allowMainThreadQueries;
        this.fallbackToDestructiveMigration = builder.fallbackToDestructiveMigration;
        this.fallbackToDestructiveMigrationFromStartVersions = builder.fallbackToDestructiveMigrationFromStartVersions;
        this.journalMode = builder.journalMode;
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

        public DatabaseConfig build() {
            return new DatabaseConfig(this);
        }
    }
}