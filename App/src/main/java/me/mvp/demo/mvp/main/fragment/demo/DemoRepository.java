package me.mvp.demo.mvp.main.fragment.demo;

import me.mvp.demo.app.api.service.ApiNetwork;
import me.mvp.demo.entity.User;
import me.mvp.demo.entity.base.ResponseEntity;
import me.mvp.frame.frame.IModel;
import me.mvp.frame.integration.IRepositoryManager;
import retrofit2.Call;

public class DemoRepository implements IModel {

    private IRepositoryManager iRepositoryManager;

    public DemoRepository(IRepositoryManager iRepositoryManager) {
        this.iRepositoryManager = iRepositoryManager;
    }

    public Call<ResponseEntity<User>> login(String username, String password) {
        return iRepositoryManager.createRetrofitService(ApiNetwork.class).login(username, password);
    }

    @Override
    public void onDestroy() {

    }
}