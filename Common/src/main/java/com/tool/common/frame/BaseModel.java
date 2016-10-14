package com.tool.common.frame;

import com.tool.common.http.BaseCacheManager;
import com.tool.common.http.BaseServiceManager;

/**
 * BaseModel
 */
public class BaseModel<C extends BaseCacheManager, S extends BaseServiceManager> {

    // 缓存管理类，管理本地或内存缓存
    protected C baseCacheManager = null;
    // 服务管理类
    protected S baseServiceManager = null;

    public BaseModel(C cacheManager, S serviceManager) {
        this.baseCacheManager = cacheManager;
        this.baseServiceManager = serviceManager;
    }

    public void onDestory() {
        baseCacheManager = null;
        baseServiceManager = null;
    }
}