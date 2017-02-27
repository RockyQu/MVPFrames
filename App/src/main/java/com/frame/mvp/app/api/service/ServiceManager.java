package com.frame.mvp.app.api.service;

import com.tool.common.http.BaseApiManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 网络通信接口管理
 */
@Singleton
public class ServiceManager implements BaseApiManager {

    private ApiCommon apiCommon;
    private ApiUser apiUser;

    /**
     * 如果需要添加service只需在构造方法中添加对应的service,在提供get方法返回出去,只要在ServiceModule提供了该service
     * Dagger2会自行注入
     *
     * @param apiCommon
     * @param apiCommon
     */
    @Inject
    public ServiceManager(ApiCommon apiCommon, ApiUser apiUser) {
        this.apiCommon = apiCommon;
        this.apiUser = apiUser;
    }

    public ApiCommon getApiCommon() {
        return apiCommon;
    }

    public ApiUser getApiUser() {
        return apiUser;
    }

    /**
     * 这里释放一些资源
     * 注意该类是单例，不需要在Activity生命周期调用
     */
    @Override
    public void onDestory() {

    }
}