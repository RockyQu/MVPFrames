package com.tool.common.frame;

import org.simple.eventbus.EventBus;

/**
 * Presenter
 */
public class BasePresenter<M extends IModel, V extends IView> implements IPresenter {

    protected M model;
    protected V view;

    public BasePresenter() {

        this.onStart();
    }

    public BasePresenter(M model) {
        this.model = model;

        this.onStart();
    }

    public BasePresenter(V view) {
        this.view = view;

        this.onStart();
    }

    public BasePresenter(M model, V view) {
        this.model = model;
        this.view = view;

        this.onStart();
    }

    /**
     * Start
     */
    @Override
    public void onStart() {
        if (useEventBus()) {
            // 注册EventBus
            EventBus.getDefault().register(this);
        }
    }

    /**
     * Destroy
     */
    @Override
    public void onDestroy() {
        if (useEventBus()) {
            // 注册EventBus
            EventBus.getDefault().register(this);
        }

        if (model != null) {
            model.onDestroy();
        }

        this.model = null;
        this.view = null;
    }

    /**
     * 使用eventBus
     *
     * @return
     */
    protected boolean useEventBus() {
        return true;
    }
}