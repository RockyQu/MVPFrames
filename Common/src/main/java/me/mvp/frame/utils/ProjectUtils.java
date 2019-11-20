package me.mvp.frame.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import me.mvp.frame.utils.base.BaseUtils;

/**
 * ProjectUtils
 */
public class ProjectUtils extends BaseUtils {

    // 目录名称
    public static String PROJECT_NAME = "MVPFrames";
    // 项目目录
    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PROJECT_NAME + File.separator;
    // 日志路径
    public static String LOG = ROOT_PATH + "log/";
    // 缓存路径
    public static String CACHE = ROOT_PATH + "cache/";

    public ProjectUtils() {
        super();
    }

    /**
     * 初始化项目文件夹
     *
     * @return
     */
    public static boolean init() {
        boolean result = true;
        if (DeviceUtils.isExitsSdcard()) {
            result = FileUtils.createOrExistsDir(ROOT_PATH);
            result = FileUtils.createOrExistsDir(LOG);
            result = FileUtils.createOrExistsDir(CACHE);
        }
        return result;
    }

    /**
     * 初始化项目文件夹
     *
     * @return
     */
    public static boolean init(String name) {
        PROJECT_NAME = !TextUtils.isEmpty(name) ? name : PROJECT_NAME;
        ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PROJECT_NAME + File.separator;
        LOG = ROOT_PATH + "log/";
        CACHE = ROOT_PATH + "cache/";
        return init();
    }
}