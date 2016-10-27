package com.frame.mvp.app;

import com.frame.mvp.model.api.Api;
import com.tool.common.base.BaseApplication;

/**
 * Application
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
