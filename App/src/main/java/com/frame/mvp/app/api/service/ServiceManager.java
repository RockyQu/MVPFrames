package com.frame.mvp.app.api.service;

import com.tool.common.http.BaseServiceManager;

import retrofit2.Retrofit;

/**
 * 网络通信接口管理
 */
public class ServiceManager implements BaseServiceManager {

    private volatile static ServiceManager serviceManager;

    // 网络通信接口管理类
    private ApiUser apiService;

    public ServiceManager(Retrofit retrofit) {
        this.apiService = retrofit.create(ApiUser.class);
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

    public ApiUser getApiService() {
        return apiService;
    }

    /**
     * 这里释放一些资源
     * 注意该类是单例，不需要在Activity生命周期调用
     */
    @Override
    public void onDestory() {

    }
}