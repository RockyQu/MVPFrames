package me.mvp.frame.db;

import android.arch.persistence.room.RoomDatabase;

public class DatabaseConfig<T extends RoomDatabase> {

    private String name;
    private String path;

    private Class<T> databaseClass;

    private boolean allowMainThreadQueries;

    public DatabaseConfig(Builder<T> builder) {
        this.name = builder.name;
        this.path = builder.path;
        this.databaseClass = builder.databaseClass;
        this.allowMainThreadQueries = builder.allowMainThreadQueries;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T extends RoomDatabase> {

        // 名称
        private String name;
        // 路径
        private String path;

        // 提供给外部模块配置自己的数据库，请传入继承自 RoomDatabase 的类
        private Class<T> databaseClass;

        // 就否允许在主线程访问数据库
        private boolean allowMainThreadQueries = false;

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
            allowMainThreadQueries = true;
            return this;
        }

        public DatabaseConfig build() {
            return new DatabaseConfig(this);
        }
    }
}