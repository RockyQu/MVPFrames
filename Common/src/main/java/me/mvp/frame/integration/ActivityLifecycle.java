package me.mvp.frame.integration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import me.mvp.frame.base.BaseFragment;
import me.mvp.frame.base.delegate.ActivityDelegate;
import me.mvp.frame.base.delegate.ActivityDelegateImpl;
import me.mvp.frame.base.delegate.IActivity;
import me.mvp.frame.base.simple.delegate.ISimpleActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 管理Activity生命周期
 */
@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Application application;

    private AppManager appManager;
    private Map<String, Object> extras;

    private FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle;
    private List<FragmentManager.FragmentLifecycleCallbacks> fragmentLifecycles;

    @Inject
    public ActivityLifecycle(Application application, AppManager appManager, Map<String, Object> extras) {
        this.application = application;
        this.appManager = appManager;
        this.extras = extras;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        appManager.addActivity(activity);

        // 配置ActivityDelegate
        if ((activity instanceof IActivity || activity instanceof ISimpleActivity) && activity.getIntent() != null) {
            ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
            if (activityDelegate == null) {
                activityDelegate = new ActivityDelegateImpl(activity);
                activity.getIntent().putExtra(ActivityDelegate.ACTIVITY_DELEGATE, activityDelegate);
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
            activity.getIntent().removeExtra(ActivityDelegate.ACTIVITY_DELEGATE);
        }
    }

    /**
     * 给每个 Activity 的所有 Fragment 设置监听其生命周期, Activity 可以通过 {@link IActivity#useFragment()}
     * 设置是否使用监听,如果这个 Activity 返回 false 的话,这个 Activity 下面的所有 Fragment 将不能使用 {@link me.mvp.frame.base.delegate.FragmentDelegate}
     * 意味着 {@link BaseFragment} 也不能使用
     *
     * @param activity
     */
    private void registerFragmentCallbacks(Activity activity) {
        boolean useFragment;
        if (activity instanceof IActivity) {
            useFragment = ((IActivity) activity).useFragment();
        } else if (activity instanceof ISimpleActivity) {
            useFragment = ((ISimpleActivity) activity).useFragment();
        } else {
            useFragment = true;
        }

        if (activity instanceof FragmentActivity && useFragment) {

            if (fragmentLifecycle == null) {
                fragmentLifecycle = new FragmentLifecycle();
            }

            ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);

            if (fragmentLifecycles == null && extras.containsKey(ConfigModule.class.getName())) {
                fragmentLifecycles = new ArrayList<>();
                List<ConfigModule> modules = (List<ConfigModule>) extras.get(ConfigModule.class.getName());
                for (ConfigModule module : modules) {
                    module.injectFragmentLifecycle(application, fragmentLifecycles);
                }
                extras.put(ConfigModule.class.getName(), null);
            }

            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : fragmentLifecycles) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
            }
        }
    }

    private ActivityDelegate fetchActivityDelegate(Activity activity) {
        ActivityDelegate activityDelegate = null;
        if ((activity instanceof IActivity || activity instanceof ISimpleActivity) && activity.getIntent() != null) {
            activityDelegate = activity.getIntent().getParcelableExtra(ActivityDelegate.ACTIVITY_DELEGATE);
        }
        return activityDelegate;
    }
}