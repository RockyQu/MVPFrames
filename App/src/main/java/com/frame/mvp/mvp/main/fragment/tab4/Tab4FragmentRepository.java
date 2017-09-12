package com.frame.mvp.mvp.main.fragment.tab4;

import com.tool.common.frame.IModel;
import com.tool.common.integration.IRepositoryManager;

/**
 * Repository
 */
public class Tab4FragmentRepository implements IModel {

    private IRepositoryManager iRepositoryManager;

    public Tab4FragmentRepository(IRepositoryManager iRepositoryManager) {
        this.iRepositoryManager = iRepositoryManager;
    }

    @Override
    public void onDestroy() {

    }
}