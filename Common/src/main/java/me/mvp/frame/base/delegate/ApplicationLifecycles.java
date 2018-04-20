package me.mvp.frame.base.delegate;

import android.app.Application;

public interface ApplicationLifecycles {

    void onCreate(Application application);

    void onTerminate(Application application);
}