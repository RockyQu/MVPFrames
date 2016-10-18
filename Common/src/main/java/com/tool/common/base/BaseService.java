package com.tool.common.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 子类实现初始化
     */
    abstract public void init();
}