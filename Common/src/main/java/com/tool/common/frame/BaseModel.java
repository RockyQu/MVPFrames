package com.tool.common.frame;

import com.tool.common.http.BaseCacheManager;
import com.tool.common.http.BaseServiceManager;

/**
 * BaseModel
 */
public class BaseModel<S extends BaseServiceManager, C extends BaseCacheManager> implements IModel {

    // 通信接口管理类
    protected S serviceManager;
    // 通信接口缓存管理类，用于管理本地或者内存缓存
    protected C cacheManager;

    public BaseModel(S serviceManager) {
        this.serviceManager = serviceManager;
    }

    public BaseModel(S serviceManager, C cacheManager) {
        this.serviceManager = serviceManager;
        this.cacheManager = cacheManager;
    }

    @Override
    public void onDestory() {
        if (serviceManager != null) {
            serviceManager = null;
        }
        if (cacheManager != null) {
            cacheManager = null;
        }
    }
}