package me.mvp.frame.frame;

import android.app.Activity;

import org.simple.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.mvp.frame.utils.ExceptionUtils;

/**
 * 基类 Presenter
 */
public class BasePresenter<M extends IModel> implements IPresenter {

    protected M model;

    protected CompositeDisposable compositeDisposable;

    public BasePresenter() {

        this.onStart();
    }

    public BasePresenter(M model) {
        ExceptionUtils.checkNotNull(model, "%s cannot be null", IModel.class.getName());
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

        // 解除订阅
        this.unDispose();

        if (model != null) {
            model.onDestroy();
        }

        this.model = null;
        this.compositeDisposable = null;
    }

    /**
     * 使用eventBus
     *
     * @return boolean
     */
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 将 {@link Disposable} 添加到 {@link CompositeDisposable} 中统一管理
     * 可在 {@link Activity#onDestroy()} 中使用 {@link #unDispose()} 停止正在执行的 RxJava 任务，避免内存泄漏(框架已自行处理)
     *
     * @param disposable
     */
    public void addDispose(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);// 将所有 Disposable 放入集中处理
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void unDispose() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();// 保证 Activity 结束时取消所有正在执行的订阅
        }
    }
}