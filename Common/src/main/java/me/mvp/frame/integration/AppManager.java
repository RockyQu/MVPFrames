package me.mvp.frame.integration;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import androidx.annotation.Nullable;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.widget.Snacker;
import me.mvp.frame.widget.Toaster;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 管理所有 Activity
 * 通过 {@link AppComponent#appManager()} 获取实例来执行对应方法
 */
@Singleton
public class AppManager {

    @Inject
    Application application;

    // true 为不需要加入到 Activity 容器进行统一管理，默认为 false
    public static final String IS_NOT_ADD_ACTIVITY_LIST = "is_not_add_activity_list";

    public static final String APPMANAGER_MESSAGE = "AppManager";
    public static final int START_ACTIVITY = 0x00000;

    public static final int SNACKER = 0x00001;
    public static final int TOASTER = 0x00002;

    public static final int EXIT = 0x00006;

    // 管理所有activity
    public List<Activity> activities;
    // 当前在前台的activity
    private Activity currentActivity;

    @Inject
    public AppManager() {
        EventBus.getDefault().register(this);
    }

    /**
     * 通过EventBus，远程执行相应方法
     */
    @Subscriber(tag = APPMANAGER_MESSAGE, mode = ThreadMode.MAIN)
    public void onReceive(Message message) {
        switch (message.what) {
            case START_ACTIVITY:
                if (message.obj == null) {
                    break;
                }

                if (message.obj instanceof Intent) {
                    startActivity((Intent) message.obj);
                } else if (message.obj instanceof Class) {
                    startActivity((Class) message.obj);
                }
                break;
            case SNACKER:
                if (message.obj == null) {
                    break;
                }

                this.snacker((String) message.obj);
                break;
            case TOASTER:
                if (message.obj == null) {
                    break;
                }

                this.toaster((String) message.obj);
                break;
            case EXIT:
                this.appExit();
                break;
            default:
                break;
        }
    }

    /**
     * {@link Snacker} 提示框
     *
     * @param message
     */
    public void snacker(String message) {
        if (this.getCurrentActivity() == null) {
            return;
        }

        Snacker.with(this.getCurrentActivity()).setMessage(message).show();
    }

    /**
     * {@link Toaster} 提示框
     *
     * @param message
     */
    public void toaster(String message) {
        if (this.getCurrentActivity() == null) {
            return;
        }

        Toaster.with(this.getCurrentActivity()).setMessage(message).show();
    }

    /**
     * 让在前台的 Activity，打开下一个 Activity
     *
     * @param intent {@link Intent}
     */
    public void startActivity(Intent intent) {
        if (getCurrentActivity() == null) {
            // 如果没有前台的 Activity 就使用 FLAG_ACTIVITY_NEW_TASK 模式启动 Activity
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
     * 将在前台的 {@link Activity} 赋值给 {@code currentActivity}, 注意此方法是在 {@link Activity#onResume} 方法执行时将栈顶的
     * {@link Activity} 赋值给 {@code currentActivity}所以在栈顶的 {@link Activity} 执行 {@link Activity#onCreate} 方法时使用
     * {@link #getCurrentActivity()} 获取的就不是当前栈顶的 {@link Activity}, 可能是上一个 {@link Activity}如果在 App 启动第一个
     * {@link Activity} 执行 {@link Activity#onCreate} 方法时使用 {@link #getCurrentActivity()} 则会出现返回为 {@code null} 的情况
     * 想避免这种情况请使用 {@link #getTopActivity()}
     *
     * @param currentActivity
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    /**
     * 获取在前台的 {@link Activity} (保证获取到的 {@link Activity} 正处于可见状态, 即未调用 {@link Activity#onStop()}),
     * 获取的 {@link Activity} 存续时间是在 {@link Activity#onStop()} 之前, 所以如果当此 {@link Activity} 调用 {@link Activity#onStop()}
     * 方法之后, 没有其他的 {@link Activity} 回到前台(用户返回桌面或者打开了其他 App 会出现此状况)
     * 这时调用 {@link #getCurrentActivity()} 有可能返回 {@code null}, 所以请注意使用场景和 {@link #getTopActivity()} 不一样
     * <p>
     * Example usage:
     * 使用场景比较适合, 只需要在可见状态的 {@link Activity} 上执行的操作
     * 如当后台 {@link Service} 执行某个任务时, 需要让前台 {@link Activity} ,做出某种响应操作或其他操作,如弹出 {@link Dialog}, 这时在 {@link Service} 中就可以使用 {@link #getCurrentActivity()}
     * 如果返回为 {@code null}, 说明没有前台 {@link Activity} (用户返回桌面或者打开了其他 App 会出现此状况), 则不做任何操作, 不为 {@code null}, 则弹出 {@link Dialog}
     *
     * @return {@link Activity}
     */
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 获取最近启动的一个 {@link Activity}, 此方法不保证获取到的 {@link Activity} 正处于前台可见状态
     * 即使 App 进入后台或在这个 {@link Activity} 中打开一个之前已经存在的 {@link Activity}, 这时调用此方法
     * 还是会返回这个最近启动的 {@link Activity}, 因此基本不会出现 {@code null} 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     * <p>
     * Tips: mActivityList 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     *
     * @return {@link Activity}
     */
    @Nullable
    public Activity getTopActivity() {
        if (activities == null) {
            return null;
        }
        return activities.size() > 0 ? activities.get(activities.size() - 1) : null;
    }

    /**
     * 返回一个存储所有未销毁的 {@link Activity} 的集合
     *
     * @return {@link Activity}
     */
    public List<Activity> getActivityList() {
        if (activities == null) {
            activities = new LinkedList<>();
        }
        return activities;
    }

    /**
     * 添加Activity到集合
     */
    public void addActivity(Activity activity) {
        synchronized (AppManager.class) {
            List<Activity> activities = getActivityList();
            if (!activities.contains(activity)) {
                activities.add(activity);
            }
        }
    }

    /**
     * 删除集合里的指定activity
     *
     * @param activity {@link Activity}
     */
    public void removeActivity(Activity activity) {
        if (activities == null) {
            return;
        }
        synchronized (AppManager.class) {
            if (activities.contains(activity)) {
                activities.remove(activity);
            }
        }
    }

    /**
     * 关闭指定的 {@link Activity} class 的所有的实例
     *
     * @param activityClass
     */
    public void killActivity(Class<?> activityClass) {
        if (activities == null) {
            return;
        }
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();

                if (next.getClass().equals(activityClass)) {
                    iterator.remove();
                    next.finish();
                }
            }
        }
    }

    /**
     * 指定的 {@link Activity} 实例是否存活
     *
     * @param activity {@link Activity}
     * @return Activity Is Live
     */
    public boolean activityInstanceIsLive(Activity activity) {
        if (activities == null) {
            return false;
        }
        return activities.contains(activity);
    }

    /**
     * 指定的 {@link Activity} class 是否存活(同一个 {@link Activity} class 可能有多个实例)
     *
     * @param activityClass
     * @return
     */
    public boolean activityClassIsLive(Class<?> activityClass) {
        if (activities == null) {
            return false;
        }
        for (Activity activity : activities) {
            if (activity.getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定 {@link Activity} class 的实例,没有则返回 null(同一个 {@link Activity} class 有多个实例,则返回最早创建的实例)
     *
     * @param activityClass
     * @return
     */
    public Activity findActivity(Class<?> activityClass) {
        if (activities == null) {
            return null;
        }
        for (Activity activity : activities) {
            if (activity.getClass().equals(activityClass)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 关闭所有 {@link Activity}
     */
    public void killAll() {
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                iterator.remove();
                next.finish();
            }
        }
    }

    /**
     * 关闭所有 {@link Activity},排除指定的 {@link Activity}
     *
     * @param excludeActivityClasses activity class
     */
    public void killAll(Class<?>... excludeActivityClasses) {
        List<Class<?>> excludeList = Arrays.asList(excludeActivityClasses);
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();

                if (excludeList.contains(next.getClass()))
                    continue;

                iterator.remove();
                next.finish();
            }
        }
    }

    /**
     * 关闭所有 {@link Activity},排除指定的 {@link Activity}
     *
     * @param excludeActivityName {@link Activity} 的完整全路径
     */
    public void killAll(String... excludeActivityName) {
        List<String> excludeList = Arrays.asList(excludeActivityName);
        synchronized (AppManager.class) {
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();

                if (excludeList.contains(next.getClass().getName()))
                    continue;

                iterator.remove();
                next.finish();
            }
        }
    }


    /**
     * 退出应用程序
     * <p>
     * 此方法经测试在某些机型上并不能完全杀死 App 进程, 几乎试过市面上大部分杀死进程的方式, 但都发现没有用，所以此
     * 方法如果不能百分之百保证能杀死进程, 就不能贸然调用 {@link #release()} 释放资源, 否则会造成其他问题。
     */
    public void appExit() {
        try {
            killAll();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        EventBus.getDefault().unregister(this);
        activities.clear();
        activities = null;
        currentActivity = null;
        application = null;
    }
}