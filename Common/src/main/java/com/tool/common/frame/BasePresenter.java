package com.tool.common.frame;

/**
 * Presenter
 */
public class BasePresenter<M, V extends BaseView> {

    protected M model;
    protected V view;

    public BasePresenter(M model, V view) {
        this.model = model;
        this.view = view;

        this.onStart();
    }

    public BasePresenter(V view) {
        this.view = view;

        this.onStart();
    }

    public BasePresenter() {
        this.onStart();
    }

    /**
     *
     */
    public void onStart() {

    }

    /**
     *
     */
    public void onDestroy() {
        this.view = null;
        this.view = null;
    }
}