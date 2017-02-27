package com.frame.mvp.app.api.cache;

import com.tool.common.http.BaseCacheManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 缓存管理
 */
@Singleton
public class CacheManager implements BaseCacheManager {

    private ApiCache apiCache;

    /**
     * 如果需要添加Cache只需在构造方法中添加对应的Cache,
     * 在提供get方法返回出去,只要在CacheModule提供了该Cache Dagger2会自行注入
     * @param apiCache
     */
    @Inject
    public CacheManager(ApiCache apiCache) {
        this.apiCache = apiCache;
    }

    public ApiCache getApiCache() {
        return apiCache;
    }

    /**
     * 这里释放一些资源
     * 注意该类是单例，不需要在Activity生命周期调用
     */
    @Override
    public void onDestory() {

    }
}