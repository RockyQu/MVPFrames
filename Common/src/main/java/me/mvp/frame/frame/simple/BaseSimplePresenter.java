package me.mvp.frame.frame.simple;

import me.mvp.frame.frame.IModel;
import me.mvp.frame.frame.IPresenter;

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

    @Override
    public void onStart() {
        if (useEventBus()) {
            // 注册EventBus
            EventBus.getDefault().register(this);
        }
    }

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