package com.tool.common.integration;

import android.content.Context;

import com.tool.common.utils.ExceptionUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;

/**
 * 用来管理网络请求层,以及数据缓存层,以后可以添加数据库请求层
 * 需要在{@link ConfigModule}的实现类中先inject需要的服务
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {

    private Retrofit retrofit;
    private final Map<String, Object> apiServices = new LinkedHashMap<>();

    @Inject
    public RepositoryManager(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * 注入RetrofitService，在{@link ConfigModule#registerComponents(Context, IRepositoryManager)}中进行注入
     *
     * @param services
     */
    @Override
    public void injectApiService(Class<?>... services) {
        for (Class<?> service : services) {
            if (apiServices.containsKey(service.getName())) {
                continue;
            }
            apiServices.put(service.getName(), retrofit.create(service));
        }
    }

    /**
     * 根据Class获取对应的RetrofitService
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public <T> T obtainApiService(Class<T> service) {
        ExceptionUtils.checkState(apiServices.containsKey(service.getName())
                , "Unable to find %s,first call injectApiService(%s) in ConfigModule", service.getName(), service.getSimpleName());
        return (T) apiServices.get(service.getName());
    }
}