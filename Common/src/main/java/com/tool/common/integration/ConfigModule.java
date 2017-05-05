package com.tool.common.integration;

import android.content.Context;

import com.tool.common.base.delegate.AppDelegateManager;
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
     * 使用{@link AppDelegateManager.Lifecycle}在Application的声明周期中注入一些操作
     *
     * @return
     */
    void injectAppLifecycle(Context context, List<AppDelegateManager.Lifecycle> lifecycleManager);
}