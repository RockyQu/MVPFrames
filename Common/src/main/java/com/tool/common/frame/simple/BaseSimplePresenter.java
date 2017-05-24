package com.tool.common.frame.simple;

import com.tool.common.frame.IModel;
import com.tool.common.frame.IPresenter;
import com.tool.common.frame.IView;

import org.simple.eventbus.EventBus;

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