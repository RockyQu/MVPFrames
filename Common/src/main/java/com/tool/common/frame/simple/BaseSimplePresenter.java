package com.tool.common.frame.simple;

import com.tool.common.frame.IModel;
import com.tool.common.frame.IPresenter;
import com.tool.common.frame.IView;

/**
 * Presenter
 */
public class BaseSimplePresenter<M extends IModel> implements IPresenter {

    protected M model;

    public BaseSimplePresenter() {

        this.onStart();
    }

    public BaseSimplePresenter(M model) {
        this.model = model;

        this.onStart();
    }

    /**
     * Start
     */
    @Override
    public void onStart() {

    }

    /**
     * Destroy
     */
    @Override
    public void onDestroy() {
        if (model != null) {
            model.onDestroy();
        }

        this.model = null;
    }
}