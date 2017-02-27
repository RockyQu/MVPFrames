package com.tool.common.frame;

import com.tool.common.http.BaseCacheManager;
import com.tool.common.http.BaseApiManager;

/**
 * BaseModel
 */
public class BaseModel<S extends BaseApiManager, C extends BaseCacheManager> implements IModel {

    // 通信接口管理类
    protected S apiManager;
    // 通信接口缓存管理类
    protected C cacheManager;

    public BaseModel(S apiManager) {
        this.apiManager = apiManager;
    }

    public BaseModel(S apiManager, C cacheManager) {
        this.apiManager = apiManager;
        this.cacheManager = cacheManager;
    }

    @Override
    public void onDestory() {
        if (apiManager != null) {
            apiManager = null;
        }
        if (cacheManager != null) {
            cacheManager = null;
        }
    }
}