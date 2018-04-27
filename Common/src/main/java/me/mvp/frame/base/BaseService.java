package me.mvp.frame.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.simple.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基类 {@link Service}
 */
public abstract class BaseService extends Service {

    protected CompositeDisposable compositeDisposable;

    public BaseService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (useEventBus()) {
            // 注册EventBus
            EventBus.getDefault().register(this);
        }

        this.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (useEventBus()) {
            // 注册EventBus
            EventBus.getDefault().unregister(this);
        }

        // 解除订阅
        unDispose();
    }

    protected void addDispose(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);// 将所有subscription放入，集中处理
    }

    protected void unDispose() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();// 保证 Activity 结束时取消所有正在执行的订阅
        }

        this.compositeDisposable = null;
    }

    /**
     * 子类实现初始化
     */
    abstract public void init();

    /**
     * 使用eventBus
     *
     * @return Whether to use EventBus.
     */
    protected boolean useEventBus() {
        return true;
    }
}