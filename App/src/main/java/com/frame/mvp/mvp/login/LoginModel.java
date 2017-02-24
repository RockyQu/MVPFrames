package com.frame.mvp.mvp.login;

import com.frame.mvp.app.api.cache.CacheManager;
import com.frame.mvp.app.api.service.ApiUser;
import com.frame.mvp.app.api.service.ServiceManager;
import com.frame.mvp.entity.User;
import com.tool.common.frame.BaseModel;
import com.tool.common.http.ResponseEntity;

import retrofit2.Call;

/**
 * LoginModel
 */
public class LoginModel extends BaseModel<ServiceManager, CacheManager> implements LoginContract.Model {

    private ApiUser apiService;

    public LoginModel(ServiceManager serviceManager) {
        super(serviceManager);
        this.apiService = serviceManager.getApiService();
    }

    @Override
    public Call<ResponseEntity<User>> login(String name, String password) {
        return apiService.login(name, password);
    }
}
