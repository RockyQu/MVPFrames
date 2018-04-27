package me.mvp.frame.frame;

/**
 * 框架要求框架中的每个 Model 都需要实现此类
 */
public interface IModel {

    /**
     * 在框架中 {@link BasePresenter#onDestroy()} 时会默认调用 {@link IModel#onDestroy()}
     */
    void onDestroy();
}