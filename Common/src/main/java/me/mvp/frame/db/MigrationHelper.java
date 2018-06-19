package me.mvp.frame.db;

/**
 * 数据库迁移工具类，当数据库升级时原有数据不丢失
 */
public class MigrationHelper {

    private volatile static MigrationHelper helper;

    private static final String SQLITE_MASTER = "sqlite_master";
    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";

    public MigrationHelper() {

    }

    /**
     * 获取实例
     */
    public static MigrationHelper getInstance() {
        if (helper == null) {
            synchronized (MigrationHelper.class) {
                if (helper == null) {
                    helper = new MigrationHelper();
                }
            }
        }
        return helper;
    }


}