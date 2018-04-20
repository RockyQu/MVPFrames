package com.frame.mvp.mvp.login;

import com.frame.mvp.app.api.service.ApiNetwork;
import com.frame.mvp.entity.User;
import com.tool.common.frame.IModel;
import com.tool.common.http.ResponseEntity;
import com.tool.common.integration.IRepositoryManager;

import retrofit2.Call;

public class LoginRepository implements IModel {

    private IRepositoryManager iRepositoryManager;

    public LoginRepository(IRepositoryManager iRepositoryManager) {
        this.iRepositoryManager = iRepositoryManager;
    }

    public Call<ResponseEntity<User>> login(String username, String password) {
        return iRepositoryManager.obtainApiService(ApiNetwork.class).login(username, password);
    }

    @Override
    public void onDestroy() {

    }
}