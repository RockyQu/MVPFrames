package com.tool.common.integration;

import android.content.Context;

import com.tool.common.frame.IModel;

/**
 * 用来管理网络请求层,以及数据缓存层
 */
public interface IRepositoryManager {

    /**
     * 根据传入的Class获取对应的Retrift service
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainApiService(Class<T> service);

    /**
     * 根据传入的Class获取对应的RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    Context getContext();
}