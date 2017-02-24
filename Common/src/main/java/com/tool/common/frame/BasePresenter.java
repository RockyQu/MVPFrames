package com.tool.common.frame;

/**
 * Presenter
 */
public class BasePresenter<M extends IModel , V extends BaseView> implements Presenter{

    protected M model;
    protected V view;

    public BasePresenter(){

        this.onStart();
    }

    public BasePresenter(V view){
        this.view = view;

        this.onStart();
    }

    public BasePresenter(M model, V view){
        this.model = model;
        this.view = view;

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
        this.model = null;
        this.view = null;
    }
}