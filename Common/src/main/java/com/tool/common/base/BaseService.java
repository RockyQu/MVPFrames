package com.tool.common.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.simple.eventbus.EventBus;

/**
 * Service
 */
public abstract class BaseService extends Service {

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