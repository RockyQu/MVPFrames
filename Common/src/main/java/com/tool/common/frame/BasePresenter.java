package com.tool.common.frame;

/**
 * Presenter
 */
public class BasePresenter<M, V> {

    protected M model;
    protected V view;

    public void setMV(M model, V view) {
        this.model = model;
        this.view = view;

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