package com.tool.common.base.delegate;

import android.app.Application;

import com.tool.common.base.App;
import com.tool.common.di.component.BaseComponent;
import com.tool.common.di.component.DaggerBaseComponent;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.di.module.ImageModule;
import com.tool.common.integration.ConfigModule;
import com.tool.common.integration.ManifestParser;

import java.util.ArrayList;
import java.util.List;

/**
 * AppDelegateManager用来代理Application以及其他应用组件的生命周期管理
 */
public class AppDelegateManager implements App {

    // Application
    private Application application = null;

    // BaseComponent
    private BaseComponent baseComponent = null;
    // ConfigModules
    private List<ConfigModule> modules = null;

    // Lifecycles
    private List<Lifecycle> lifecycleManager = new ArrayList<>();

    public AppDelegateManager(Application application) {
        this.application = application;

        modules = new ManifestParser(application).parse();
        for (ConfigModule module : modules) {
            module.injectAppLifecycle(application, lifecycleManager);
        }
    }

    public void onCreate() {
        baseComponent = DaggerBaseComponent
                .builder()
                .appModule(new AppModule(application))
                .httpModule(new HttpModule())// Http模块
                .imageModule(new ImageModule())// 图片模块
                .appConfigModule(getAppConfigModule(application, modules))// 全局配置
                .build();
        baseComponent.inject(this);

        for (ConfigModule module : modules) {
            module.registerComponents(application, baseComponent.getRepositoryManager());
        }

        for (Lifecycle lifecycle : lifecycleManager) {
            lifecycle.onCreate(application);
        }
    }

    public void onTerminate() {

        this.baseComponent = null;
        this.application = null;

        for (Lifecycle lifecycle : lifecycleManager) {
            lifecycle.onTerminate(application);
        }
    }


    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private AppConfigModule getAppConfigModule(Application context, List<ConfigModule> modules) {
        AppConfigModule.Builder builder = AppConfigModule
                .builder();

        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */

    @Override
    public BaseComponent getBaseComponent() {
        return baseComponent;
    }

    public interface Lifecycle {

        void onCreate(Application application);

        void onTerminate(Application application);
    }
}