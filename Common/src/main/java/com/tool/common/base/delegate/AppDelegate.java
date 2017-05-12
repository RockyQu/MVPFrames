package com.tool.common.base.delegate;

import android.app.Application;

import com.tool.common.base.App;
import com.tool.common.di.component.AppComponent;
import com.tool.common.di.component.DaggerAppComponent;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.integration.ActivityLifecycle;
import com.tool.common.integration.ConfigModule;
import com.tool.common.integration.ManifestParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * AppDelegateManager用来代理Application以及其他应用组件的生命周期管理
 */
public class AppDelegate implements App {

    // Application
    private Application application = null;

    // BaseComponent
    private AppComponent component = null;

    @Inject
    protected ActivityLifecycle activityLifecycle = null;

    // ConfigModules
    private List<ConfigModule> modules = null;

    // Lifecycles
    private List<Lifecycle> applicationLifecycles = new ArrayList<>();
    // Activity Lifecycles
    private List<Application.ActivityLifecycleCallbacks> activityLifecycles = new ArrayList<>();


    public AppDelegate(Application application) {
        this.application = application;

        modules = new ManifestParser(application).parse();
        for (ConfigModule module : modules) {
            module.injectAppLifecycle(application, applicationLifecycles);
            module.injectActivityLifecycle(application, activityLifecycles);
        }
    }

    public void onCreate() {
        component = DaggerAppComponent
                .builder()
                .appModule(new AppModule(application))
                .httpModule(new HttpModule())// Http模块
                .appConfigModule(getAppConfigModule(application, modules))// 全局配置
                .build();
        component.inject(this);

        // 存放配置接口ConfigModule
        component.extras().put(ConfigModule.class.getName(), modules);

        // 注入Activity生命周期
        application.registerActivityLifecycleCallbacks(activityLifecycle);
        for (Application.ActivityLifecycleCallbacks lifecycle : activityLifecycles) {
            application.registerActivityLifecycleCallbacks(lifecycle);
        }

        // 注入API接口
        for (ConfigModule module : modules) {
            module.registerComponents(application, component.getRepositoryManager());
        }

        // 注入Application生命周期
        for (Lifecycle lifecycle : applicationLifecycles) {
            lifecycle.onCreate(application);
        }
    }

    public void onTerminate() {
        if (activityLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(activityLifecycle);
        }
        if (activityLifecycles != null && activityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : activityLifecycles) {
                application.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }

        for (Lifecycle lifecycle : applicationLifecycles) {
            lifecycle.onTerminate(application);
        }

        this.component = null;
        this.activityLifecycle = null;
        this.activityLifecycles = null;
        this.applicationLifecycles = null;
        this.application = null;
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
    public AppComponent getAppComponent() {
        return component;
    }

    public interface Lifecycle {

        void onCreate(Application application);

        void onTerminate(Application application);
    }
}