package me.mvp.demo.mvp.login;

import me.mvp.demo.app.api.service.ApiNetwork;
import me.mvp.demo.entity.User;
import me.mvp.frame.frame.IModel;
import me.mvp.frame.http.ResponseEntity;
import me.mvp.frame.integration.IRepositoryManager;

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