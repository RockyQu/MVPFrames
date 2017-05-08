package com.tool.common.integration;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 管理Activity生命周期
 */
@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private AppManager appManager;

    @Inject
    public ActivityLifecycle(AppManager appManager) {
        this.appManager = appManager;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        appManager.addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        appManager.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (appManager.getCurrentActivity() == activity) {
            appManager.setCurrentActivity(null);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        appManager.removeActivity(activity);
    }
}