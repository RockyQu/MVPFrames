package com.tool.common.integration;

import android.content.Context;

/**
 * 用来管理网络请求层,以及数据缓存层,以后可以添加数据库请求层
 * 需要在{@link ConfigModule}的实现类中先inject需要的服务
 */
public interface IRepositoryManager {

    /**
     * 注入RetrofitService，在{@link ConfigModule#registerComponents(Context, IRepositoryManager)}中进行注入
     *
     * @param services
     */
    void injectApiService(Class<?>... services);

    /**
     * 根据Class获取对应的RetrofitService
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainApiService(Class<T> service);
}