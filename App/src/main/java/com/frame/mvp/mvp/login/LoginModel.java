package com.frame.mvp.mvp.login;

import com.frame.mvp.app.api.cache.CacheManager;
import com.frame.mvp.app.api.service.ServiceManager;
import com.tool.common.frame.BaseModel;

/**
 *
 */
public class LoginModel extends BaseModel<CacheManager, ServiceManager> {

    public LoginModel(CacheManager cacheManager, ServiceManager serviceManager) {
        super(cacheManager, serviceManager);
    }
}
