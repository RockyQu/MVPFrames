package com.tool.common.frame;

import com.tool.common.integration.IRepositoryManager;

/**
 * BaseModel
 */
public class BaseModel implements IModel {

    // 用于管理通信接口、缓存数据
    protected IRepositoryManager repositoryManager;

    public BaseModel(IRepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Override
    public void onDestroy() {
        repositoryManager = null;
    }
}