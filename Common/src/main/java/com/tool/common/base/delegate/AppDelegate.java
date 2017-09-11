package com.tool.common.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import com.tool.common.base.App;
import com.tool.common.di.component.AppComponent;
import com.tool.common.di.component.DaggerAppComponent;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.integration.ActivityLifecycle;
import com.tool.common.integration.ConfigModule;
import com.tool.common.integration.ManifestParser;
import com.tool.common.widget.imageloader.glide.GlideImageConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * AppDelegateManager用来代理Application以及其他应用组件的生命周期管理
 */
public class AppDelegate implements App, ApplicationLifecycles {

    // Application
    private Application application = null;

    // BaseComponent
    private AppComponent component = null;

    @Inject
    protected ActivityLifecycle activityLifecycle = null;

    // ConfigModules
    private List<ConfigModule> modules = null;

    // Lifecycles
    private List<ApplicationLifecycles> applicationLifecycles = new ArrayList<>();
    // Activity Lifecycles
    private List<Application.ActivityLifecycleCallbacks> activityLifecycles = new ArrayList<>();

    // 这是一个细粒度的内存回收管理回调
    // Application、Activity、Service、ContentProvider、Fragment实现了ComponentCallback2接口
    // 开发者应该实现 onTrimMemory(int) 方法，细粒度 release 内存，参数可以体现不同程度的内存可用情况
    // 响应 onTrimMemory 回调：开发者的 App 会直接受益，有利于用户体验，系统更有可能让 App 存活的更持久
    // 不响应 onTrimMemory 回调：系统更有可能 Kill 进程
    private ComponentCallbacks2 componentCallbacks;

    public AppDelegate(Context context) {
        modules = new ManifestParser(context).parse();
        for (ConfigModule module : modules) {
            module.injectAppLifecycle(context, applicationLifecycles);
            module.injectActivityLifecycle(context, activityLifecycles);
        }
    }

//    @Override
//    public void attachBaseContext(Context baseContext) {
//        for (ApplicationLifecycles lifecycle : applicationLifecycles) {
//            lifecycle.attachBaseContext(baseContext);
//        }
//    }

    @Override
    public void onCreate(Application application) {
        this.application = application;
        component = DaggerAppComponent
                .builder()
                .appModule(new AppModule(application))
                .httpModule(new HttpModule())// Http模块
                .appConfigModule(getAppConfigModule(application, modules))// 全局配置
                .build();
        component.inject(this);

        // 存放配置接口ConfigModule
        component.extras().put(ConfigModule.class.getName(), modules);

        this.modules = null;

        // 注入Activity生命周期
        application.registerActivityLifecycleCallbacks(activityLifecycle);
        for (Application.ActivityLifecycleCallbacks lifecycle : activityLifecycles) {
            application.registerActivityLifecycleCallbacks(lifecycle);
        }

        // 内存回收管理接口
        componentCallbacks = new AppComponentCallbacks(application, component);
        application.registerComponentCallbacks(componentCallbacks);

        // 注入Application生命周期
        for (ApplicationLifecycles lifecycle : applicationLifecycles) {
            lifecycle.onCreate(application);
        }
    }

    @Override
    public void onTerminate(Application application) {
        if (activityLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(activityLifecycle);
        }

        if (componentCallbacks != null) {
            application.unregisterComponentCallbacks(componentCallbacks);
        }

        if (activityLifecycles != null && activityLifecycles.size() != 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : activityLifecycles) {
                application.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }

        for (ApplicationLifecycles lifecycle : applicationLifecycles) {
            lifecycle.onTerminate(application);
        }

        this.component = null;
        this.activityLifecycle = null;
        this.activityLifecycles = null;
        this.applicationLifecycles = null;
        this.componentCallbacks = null;
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
     * @return {@link AppComponent}
     */
    @Override
    public AppComponent getAppComponent() {
        return component;
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {

        private Application application;
        private AppComponent appComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.application = application;
            this.appComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            // 内存不足时清理图片请求框架的内存缓存
            appComponent.getImageLoader().clear(application, GlideImageConfig
                    .builder()
                    .isClearMemory(true)
                    .build());
        }
    }
}