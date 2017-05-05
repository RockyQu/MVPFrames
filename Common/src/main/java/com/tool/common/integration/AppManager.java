package com.tool.common.integration;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 管理所有Activity，和在前台的Activity
 * 可以通过直接持有AppManager对象执行对应方法
 */
@Singleton
public class AppManager {

    // Application
    private Application application;

    // 管理所有activity
    public List<Activity> activitys;
    // 当前在前台的activity
    private Activity currentActivity;

    @Inject
    public AppManager(Application application) {
        this.application = application;
    }

    /**
     * 让在前台的Activity,打开下一个Activity
     *
     * @param intent
     */
    public void startActivity(Intent intent) {
        if (getCurrentActivity() == null) {
            //如果没有前台的activity就使用new_task模式启动activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
            return;
        }
        getCurrentActivity().startActivity(intent);
    }

    /**
     * 让在前台的Activity,打开下一个Activity
     *
     * @param activityClass
     */
    public void startActivity(Class activityClass) {
        startActivity(new Intent(application, activityClass));
    }

    /**
     * 将在前台的activity保存
     *
     * @param currentActivity
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    /**
     * 获得当前在前台的activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 返回一个存储所有未销毁的activity的集合
     *
     * @return
     */
    public List<Activity> getActivityList() {
        if (activitys == null) {
            activitys = new LinkedList<>();
        }
        return activitys;
    }


    /**
     * 添加Activity到集合
     */
    public void addActivity(Activity activity) {
        if (activitys == null) {
            activitys = new LinkedList<>();
        }
        synchronized (AppManager.class) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
            }
        }
    }

    /**
     * 删除集合里的指定activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activitys == null) {
            return;
        }
        synchronized (AppManager.class) {
            if (activitys.contains(activity)) {
                activitys.remove(activity);
            }
        }
    }

    /**
     * 删除集合里的指定位置的activity
     *
     * @param location
     */
    public Activity removeActivity(int location) {
        if (activitys == null) {
            return null;
        }
        synchronized (AppManager.class) {
            if (location > 0 && location < activitys.size()) {
                return activitys.remove(location);
            }
        }
        return null;
    }

    /**
     * 关闭指定activity
     *
     * @param activityClass
     */
    public void killActivity(Class<?> activityClass) {
        if (activitys == null) {
            return;
        }
        for (Activity activity : activitys) {
            if (activity.getClass().equals(activityClass)) {
                activity.finish();
            }
        }
    }


    /**
     * 指定的activity实例是否存活
     *
     * @param activity
     * @return
     */
    public boolean activityInstanceIsLive(Activity activity) {
        if (activitys == null) {
            return false;
        }
        return activitys.contains(activity);
    }

    /**
     * 指定的Activity是否存活（一个activity可能有多个实例）
     *
     * @param activityClass
     * @return
     */
    public boolean activityClassIsLive(Class<?> activityClass) {
        if (activitys == null) {
            return false;
        }
        for (Activity activity : activitys) {
            if (activity.getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭所有activity
     */
    public void killAll() {
        Iterator<Activity> iterator = getActivityList().iterator();
        while (iterator.hasNext()) {
            iterator.next().finish();
            iterator.remove();
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            killAll();
            if (activitys != null){
                activitys = null;
            }
            ActivityManager activityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(application.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        activitys.clear();
        activitys = null;
        currentActivity = null;
        application = null;
    }
}