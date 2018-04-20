package me.mvp.frame.integration;

import android.content.Context;

import me.mvp.frame.frame.IModel;

/**
 * 用来管理网络请求层,以及数据缓存层
 */
public interface IRepositoryManager {

    /**
     * 根据传入的 Class 创建对应的仓库，这个方法在非注入方式下用到
     *
     * @param repository
     * @param <T>
     * @return
     */
    <T extends IModel> T createRepository(Class<T> repository);

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