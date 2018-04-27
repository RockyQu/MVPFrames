package me.mvp.frame.integration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import me.mvp.frame.base.BaseFragment;
import me.mvp.frame.base.delegate.ActivityDelegate;
import me.mvp.frame.base.delegate.ActivityDelegateImpl;
import me.mvp.frame.base.IActivity;
import me.mvp.frame.base.delegate.FragmentDelegate;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link Application.ActivityLifecycleCallbacks} 默认实现类
 * 通过 {@link ActivityDelegate} 管理 {@link Activity}
 */
@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Application application;

    private AppManager appManager;
    private Cache<String, Object> extras;

    private FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle;
    private List<FragmentManager.FragmentLifecycleCallbacks> fragmentLifecycles;

    @Inject
    public ActivityLifecycle(Application application, AppManager appManager, Cache<String, Object> extras) {
        this.application = application;
        this.appManager = appManager;
        this.extras = extras;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // 如果 Intent 包含了此字段，并且为 true 则不加入到 AppManager 进行统一管理
        boolean isNotAdd = false;
        if (activity.getIntent() != null) {
            isNotAdd = activity.getIntent().getBooleanExtra(AppManager.IS_NOT_ADD_ACTIVITY_LIST, false);
        }

        if (!isNotAdd) {
            appManager.addActivity(activity);
        }

        // 配置ActivityDelegate
        if (activity instanceof IActivity && activity.getIntent() != null) {
            ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
            if (activityDelegate == null) {
                Cache<String, Object> cache = getCacheFromActivity((IActivity) activity);
                activityDelegate = new ActivityDelegateImpl(activity);
                cache.put(ActivityDelegate.ACTIVITY_DELEGATE, activityDelegate);
            }
            activityDelegate.onCreate(savedInstanceState);
        }

        // 配置Fragment生命周期监听
        registerFragmentCallbacks(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        appManager.setCurrentActivity(activity);

        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (appManager.getCurrentActivity() == activity) {
            appManager.setCurrentActivity(null);
        }

        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onSaveInstanceState(bundle);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        appManager.removeActivity(activity);

        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onDestroy();
            getCacheFromActivity((IActivity) activity).clear();
        }
    }

    /**
     * 给每个 Activity 的所有 Fragment 设置监听其生命周期, Activity 可以通过 {@link IActivity#useFragment()}
     * 设置是否使用监听,如果这个 Activity 返回 false 的话,这个 Activity 下面的所有 Fragment 将不能使用 {@link FragmentDelegate}
     * 意味着 {@link BaseFragment} 也不能使用
     *
     * @param activity
     */
    private void registerFragmentCallbacks(Activity activity) {
        boolean useFragment = !(activity instanceof IActivity) || ((IActivity) activity).useFragment();
        if (activity instanceof FragmentActivity && useFragment) {

            if (fragmentLifecycle == null) {
                // Fragment 生命周期实现类，用于框架内部对每个 Fragment 的必要操作，如给每个 Fragment 配置 FragmentDelegate
                fragmentLifecycle = new FragmentLifecycle();
            }

            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);

            if (fragmentLifecycles == null && extras.containsKey(ConfigModule.class.getName())) {
                fragmentLifecycles = new ArrayList<>();
                List<ConfigModule> modules = (List<ConfigModule>) extras.get(ConfigModule.class.getName());
                for (ConfigModule module : modules) {
                    module.injectFragmentLifecycle(application, fragmentLifecycles);
                }
                extras.remove(ConfigModule.class.getName());
            }

            // 注册框架外部, 用于扩展的 Fragment 生命周期相关逻辑
            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : fragmentLifecycles) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
            }
        }
    }

    private ActivityDelegate fetchActivityDelegate(Activity activity) {
        ActivityDelegate activityDelegate = null;
        if (activity instanceof IActivity) {
            Cache<String, Object> cache = getCacheFromActivity((IActivity) activity);
            activityDelegate = (ActivityDelegate) cache.get(ActivityDelegate.ACTIVITY_DELEGATE);
        }
        return activityDelegate;
    }

    @NonNull
    private Cache<String, Object> getCacheFromActivity(IActivity activity) {
        Cache<String, Object> cache = activity.provideCache();
        ExceptionUtils.checkNotNull(cache, "%s cannot be null on Activity", Cache.class.getName());
        return cache;
    }
}