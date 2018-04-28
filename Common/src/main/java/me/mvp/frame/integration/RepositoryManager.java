package me.mvp.frame.integration;

import android.app.Application;
import android.content.Context;

import me.mvp.frame.frame.IModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import me.mvp.frame.integration.cache.Cache;
import me.mvp.frame.integration.cache.CacheType;
import me.mvp.frame.utils.ExceptionUtils;
import retrofit2.Retrofit;

/**
 * 用来管理网络请求层，以及数据缓存层
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {

    @Inject
    Application application;

    @Inject
    Lazy<Retrofit> retrofit;

    @Inject
    Lazy<RxCache> rxCache;

    @Inject
    Cache.Factory cachefactory;

    private Cache<String, IModel> repositoryCache = null;
    private Cache<String, Object> retrofitServiceCache = null;
    private Cache<String, Object> cacheServiceCache = null;

    @Inject
    public RepositoryManager() {

    }

    /**
     * 根据传入的 Class 创建对应的仓库
     *
     * @param repository
     * @param <T>
     * @return Model
     */
    @Override
    public synchronized <T extends IModel> T createRepository(Class<T> repository) {
        if (repositoryCache == null) {
            repositoryCache = cachefactory.build(CacheType.REPOSITORY_CACHE);
        }

        ExceptionUtils.checkNotNull(repositoryCache, "Cannot return null from a Cache.Factory#build(int) method");
        T repositoryInstance = (T) repositoryCache.get(repository.getCanonicalName());
        if (repositoryInstance == null) {
            Constructor<? extends IModel> constructor = findConstructorForClass(repository);
            try {
                repositoryInstance = (T) constructor.newInstance(this);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to invoke " + constructor, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to invoke " + constructor, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Create repository error", e);
            }
            repositoryCache.put(repository.getCanonicalName(), repositoryInstance);
        }
        return repositoryInstance;
    }

    /**
     * 根据传入的 Class 创建对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return Retrofit Service
     */
    @Override
    public synchronized <T> T createRetrofitService(Class<T> service) {
        if (retrofitServiceCache == null) {
            retrofitServiceCache = cachefactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }

        ExceptionUtils.checkNotNull(retrofitServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) retrofitServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = retrofit.get().create(service);
            retrofitServiceCache.put(service.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    /**
     * 根据传入的 Class 创建对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return Cache Service
     */
    @Override
    public synchronized <T> T createCacheService(Class<T> cache) {
        if (cacheServiceCache == null) {
            cacheServiceCache = cachefactory.build(CacheType.CACHE_SERVICE_CACHE);
        }

        ExceptionUtils.checkNotNull(cacheServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) cacheServiceCache.get(cache.getCanonicalName());
        if (cacheService == null) {
            cacheService = rxCache.get().using(cache);
            cacheServiceCache.put(cache.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    private Constructor<? extends IModel> findConstructorForClass(Class<?> cls) {
        Constructor<? extends IModel> bindingCtor;
        String clsName = cls.getName();
        try {
            // noinspection unchecked
            bindingCtor = (Constructor<? extends IModel>) cls.getConstructor(IRepositoryManager.class);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for " + clsName, e);
        }
        return bindingCtor;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        this.rxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return application;
    }
}