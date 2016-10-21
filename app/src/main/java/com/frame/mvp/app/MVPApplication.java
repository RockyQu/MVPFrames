package com.frame.mvp.app;

import com.frame.mvp.model.api.Api;
import com.tool.common.base.BaseApplication;

/**
 * Application
 *
 * 配置自定义Application必须继承BaseApplication，BaseApplication完成Http框架、图片框架、日志管理框架等初始化工作。
 */
public class MVPApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public String getJavaUrl() {
        return Api.URL_BASE_JAVA;
    }
}
