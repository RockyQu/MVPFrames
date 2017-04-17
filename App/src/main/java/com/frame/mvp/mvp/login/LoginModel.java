package com.frame.mvp.mvp.login;

import com.frame.mvp.app.api.service.ApiUser;
import com.frame.mvp.entity.User;
import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BaseModel;
import com.tool.common.http.ResponseEntity;
import com.tool.common.integration.IRepositoryManager;

import javax.inject.Inject;

import retrofit2.Call;

/**
 * LoginModel
 */
@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Call<ResponseEntity<User>> login(String name, String password) {
        return repositoryManager.obtainApiService(ApiUser.class).login(name, password);
    }
}
