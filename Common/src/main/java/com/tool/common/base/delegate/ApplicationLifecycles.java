package com.tool.common.base.delegate;

import android.app.Application;
import android.content.Context;

public interface ApplicationLifecycles {

    void attachBaseContext(Context baseContext);

    void onCreate(Application application);

    void onTerminate(Application application);
}