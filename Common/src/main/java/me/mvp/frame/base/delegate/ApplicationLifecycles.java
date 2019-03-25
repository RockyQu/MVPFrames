package me.mvp.frame.base.delegate;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * {@link Application} 生命周期代理接口
 */
public interface ApplicationLifecycles {

    void attachBaseContext(@NonNull Context base);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);
}