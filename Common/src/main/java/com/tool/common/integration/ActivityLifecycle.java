package com.tool.common.integration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.tool.common.base.BaseFragment;
import com.tool.common.base.delegate.ActivityDelegate;
import com.tool.common.base.delegate.ActivityDelegateImpl;
import com.tool.common.base.delegate.IActivity;
import com.tool.common.base.simple.delegate.ISimpleActivity;

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

    private FragmentLifecycle fragmentLifecycle;
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

        /**
         * 给每个Activity配置Fragment的监听，Activity可以通过 {@link IActivity#useFragment()}设置是否使用监听
         * 如果这个Activity返回false的话，这个Activity将不能使用{@link FragmentDelegate}，意味着{@link BaseFragment}也不能使用
         */
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

            if (fragmentLifecycles == null) {
                fragmentLifecycles = new ArrayList<>();
                List<ConfigModule> modules = (List<ConfigModule>) extras.get(ConfigModule.class.getName());
                for (ConfigModule module : modules) {
                    module.injectFragmentLifecycle(application, fragmentLifecycles);
                }
            }

            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : fragmentLifecycles) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
            }
        }
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
        if (appManager.getCurrentActivity() == activity) {
            appManager.setCurrentActivity(null);
        }

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

        boolean useFragment;
        if (activity instanceof IActivity) {
            useFragment = ((IActivity) activity).useFragment();
        } else if (activity instanceof ISimpleActivity) {
            useFragment = ((ISimpleActivity) activity).useFragment();
        } else {
            useFragment = true;
        }

        if (activity instanceof FragmentActivity && useFragment) {
            if (fragmentLifecycle != null) {
                ((FragmentActivity) activity).getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentLifecycle);
            }
            if (fragmentLifecycles != null && fragmentLifecycles.size() > 0) {
                for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : fragmentLifecycles) {
                    ((FragmentActivity) activity).getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentLifecycle);
                }
            }
        }

        ActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onDestroy();
            activity.getIntent().removeExtra(ActivityDelegate.ACTIVITY_DELEGATE);
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