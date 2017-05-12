package com.frame.mvp.mvp.main;

import com.tool.common.di.scope.ActivityScope;
import com.tool.common.frame.BaseModel;
import com.tool.common.integration.IRepositoryManager;

import javax.inject.Inject;

/**
 * LoginModel
 */
@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {

    @Inject
    public MainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
}
