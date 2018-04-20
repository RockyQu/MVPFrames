package com.frame.mvp.mvp.main;

import com.tool.common.frame.IModel;
import com.tool.common.integration.IRepositoryManager;

public class MainRepository implements IModel {

    private IRepositoryManager iRepositoryManager;

    public MainRepository(IRepositoryManager iRepositoryManager) {
        this.iRepositoryManager = iRepositoryManager;
    }

    @Override
    public void onDestroy() {

    }
}