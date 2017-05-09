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

        if (ProjectUtils.init()) {
            // 设置反馈崩溃信息，不需要可以不设置
            ThreadCatchInterceptor.getInstance().install(new ThreadCatchInterceptor.CallBack() {

                @Override
                public void error(Throwable throwable) {
                    ;
                }

                @Override
                public void finish(String path) {
                    QLog.i(path);
                }
            });
        } else {
            // 初始化失败
        }
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