package com.tool.common.integration;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.tool.common.base.delegate.AppDelegate;
import com.tool.common.di.module.AppConfigModule;

import java.util.List;


/**
 * 框架配置参数配置，实现此接口后，在AndroidManifest中声明该实现类
 */
public interface ConfigModule {

    /**
     * 使用{@link AppConfigModule.Builder}给框架配置参数
     *
     * @param context
     * @param builder
     */
    void applyOptions(Context context, AppConfigModule.Builder builder);

    /**
     * 使用{@link IRepositoryManager}给框架注入网络请求和数据缓存等服务
     *
     * @param context
     * @param repositoryManager
     */
    void registerComponents(Context context, IRepositoryManager repositoryManager);

    /**
     * 使用{@link AppDelegate.Lifecycle}在Application的声明周期中注入一些操作
     *
     * @return
     */
    void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles);

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles);


    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles);
}