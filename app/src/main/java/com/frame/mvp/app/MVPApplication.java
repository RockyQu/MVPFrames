package com.frame.mvp.app;

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

    /**
     * 获得当前网络状态
     * <p>
     * NONE(1) 无网络，MOBILE(2)移动网络，WIFI(4)无线网络
     *
     * @return
     */
    public static NetworkStatusReceiver.Type getNetworkType() {
        return NetworkStatusReceiver.getType(getContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}