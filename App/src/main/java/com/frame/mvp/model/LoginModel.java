package com.frame.mvp.model;

import com.frame.mvp.model.api.cache.CacheManager;
import com.frame.mvp.model.api.service.ServiceManager;
import com.tool.common.frame.BaseModel;

/**
 *
 */
public class LoginModel extends BaseModel<CacheManager, ServiceManager> {

    public LoginModel(CacheManager cacheManager, ServiceManager serviceManager) {
        super(cacheManager, serviceManager);
    }
}
