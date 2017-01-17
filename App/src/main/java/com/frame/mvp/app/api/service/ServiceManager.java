package com.frame.mvp.app.api.service;

import com.tool.common.http.BaseServiceManager;

import retrofit2.Retrofit;

/**
 * 网络通信接口管理
 */
public class ServiceManager extends BaseServiceManager {

    private volatile static ServiceManager serviceManager;

    // 网络通信接口管理类
    private ApiService apiService;

    public ServiceManager(Retrofit retrofit) {
        this.apiService = retrofit.create(ApiService.class);
    }

    public static ServiceManager getInstance(Retrofit retrofit) {
        if (serviceManager == null) {
            synchronized (ServiceManager.class) {
                if (serviceManager == null) {
                    serviceManager = new ServiceManager(retrofit);
                }
            }
        }
        return serviceManager;
    }

    public ApiService getApiService() {
        return apiService;
    }
}