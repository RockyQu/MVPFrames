package com.frame.mvp.app.api.cache;

import com.tool.common.http.BaseCacheManager;

/**
 * 缓存管理
 */
public class CacheManager implements BaseCacheManager {

    /**
     * 这里释放一些资源
     * 注意该类是单例，不需要在Activity生命周期调用
     */
    @Override
    public void onDestory() {

    }
}