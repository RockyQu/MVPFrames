package com.frame.mvp.app;

import android.content.Context;

import com.tool.common.base.BaseApplication;
import com.tool.common.http.receiver.NetworkStatusReceiver;
import com.tool.common.log.QLog;
import com.tool.common.log.crash.ThreadCatchInterceptor;
import com.tool.common.utils.ProjectUtils;

/**
 * Application
 */
public class MVPApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}