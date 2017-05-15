package com.tool.common.integration;

import android.content.Context;

import com.tool.common.frame.IModel;
import com.tool.common.utils.ExceptionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    private final Map<String, IModel> mRepositoryCache = new LinkedHashMap<>();
    private final Map<String, Object> apiServices = new LinkedHashMap<>();

    @Inject
    public RepositoryManager(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * 根据传入的Class创建对应的Model
     *
     * @param repository
     * @param <T>
     * @return
     */
    @Override
    public <T extends IModel> T createRepository(Class<T> repository) {
        T repositoryInstance;
        synchronized (mRepositoryCache) {
            repositoryInstance = (T) mRepositoryCache.get(repository.getName());
            if (repositoryInstance == null) {
                Constructor<? extends IModel> constructor = findConstructorForClass(repository);
                try {
                    repositoryInstance = (T) constructor.newInstance(this);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Unable to invoke " + constructor, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to invoke " + constructor, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("create repository error", e);
                }
                mRepositoryCache.put(repository.getName(), repositoryInstance);
            }
        }
        return repositoryInstance;
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

    private static Constructor<? extends IModel> findConstructorForClass(Class<?> cls) {
        Constructor<? extends IModel> bindingCtor;

        String clsName = cls.getName();

        try {
            //noinspection unchecked
            bindingCtor = (Constructor<? extends IModel>) cls.getConstructor(IRepositoryManager.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for " + clsName, e);
        }

        return bindingCtor;
    }
}