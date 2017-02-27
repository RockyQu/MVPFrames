package com.frame.mvp.mvp.login;

import com.frame.mvp.app.api.cache.CacheManager;
import com.frame.mvp.app.api.service.ApiUser;
import com.frame.mvp.app.api.service.ServiceManager;
import com.frame.mvp.entity.User;
import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BaseModel;
import com.tool.common.http.ResponseEntity;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * LoginModel
 */
@ActivityScope
public class LoginModel extends BaseModel<ServiceManager, CacheManager> implements LoginContract.Model {

    @Inject
    public LoginModel(ServiceManager apiManager) {
        super(apiManager);
    }

    @Override
    public Call<ResponseEntity<User>> login(String name, String password) {
        return apiManager.getApiUser().login(name, password);
    }
}
