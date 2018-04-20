package me.mvp.demo.mvp.main;

import me.mvp.frame.frame.IModel;
import me.mvp.frame.integration.IRepositoryManager;

public class MainRepository implements IModel {

    private IRepositoryManager iRepositoryManager;

    public MainRepository(IRepositoryManager iRepositoryManager) {
        this.iRepositoryManager = iRepositoryManager;
    }

    @Override
    public void onDestroy() {

    }
}