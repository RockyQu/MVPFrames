package me.mvp.frame.base.delegate;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentCallbacks2;
import android.content.ContentProvider;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.mvp.frame.base.App;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.di.component.DaggerAppComponent;
import me.mvp.frame.di.module.AppConfigModule;
import me.mvp.frame.di.module.AppModule;
import me.mvp.frame.di.module.HttpModule;
import me.mvp.frame.integration.ActivityLifecycle;
import me.mvp.frame.integration.ConfigModule;
import me.mvp.frame.integration.ManifestParser;

/**
 * {@link ApplicationLifecycles} Application 生命周期代理实现类
 */
public class ApplicationDelegate implements App, ApplicationLifecycles {

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

    public ApplicationDelegate(Context context) {
        modules = new ManifestParser(context).parse();
        for (ConfigModule module : modules) {

            // 将框架外部，开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 applicationLifecycles 集合 (此时还未注册回调)
            module.injectApplicationLifecycle(context, applicationLifecycles);

            // 将框架外部，开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 activityLifecycles 集合 (此时还未注册回调)
            module.injectActivityLifecycle(context, activityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(Context base) {
        for (ApplicationLifecycles lifecycle : applicationLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

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

        // 注册框架内部已实现的 Activity 生命周期逻辑
        application.registerActivityLifecycleCallbacks(activityLifecycle);

        // 注册框架外部，开发者扩展的 Activity 生命周期逻辑
        // 每个 ConfigModule 的实现类可以声明多个 Activity 的生命周期回调
        // 也可以有 N 个 ConfigModule 的实现类 (完美支持组件化项目各个 Module 的各种独特需求)
        for (Application.ActivityLifecycleCallbacks lifecycle : activityLifecycles) {
            application.registerActivityLifecycleCallbacks(lifecycle);
        }

        // 内存回收管理接口
        componentCallbacks = new AppComponentCallbacks(application, component);
        // 内存紧张时释放部分内存
        application.registerComponentCallbacks(componentCallbacks);

        // 执行框架外部，开发者扩展的 Application onCreate 逻辑
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
     * 将 App 的全局配置信息封装进 Module (使用Dagger注入到需要配置信息的地方)
     * 需要在 AndroidManifest 中声明 {@link ConfigModule} 的实现类
     *
     * @return AppConfigModule
     */
    private AppConfigModule getAppConfigModule(Application context, List<ConfigModule> modules) {
        AppConfigModule.Builder builder = AppConfigModule
                .builder();

        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        return component;
    }

    /**
     * {@link ComponentCallbacks2} 是一个细粒度的内存回收管理回调
     * {@link Application}、{@link Activity}、{@link Service}、{@link ContentProvider}、{@link Fragment} 实现了 {@link ComponentCallbacks2} 接口
     * 开发者应该实现 {@link ComponentCallbacks2#onTrimMemory(int)} 方法, 细粒度 release 内存, 参数的值不同可以体现出不同程度的内存可用情况
     * 响应 {@link ComponentCallbacks2#onTrimMemory(int)} 回调, 开发者的 App 会存活的更持久, 有利于用户体验
     * 不响应 {@link ComponentCallbacks2#onTrimMemory(int)} 回调, 系统 kill 掉进程的几率更大
     */
    private static class AppComponentCallbacks implements ComponentCallbacks2 {

        private Application application;
        private AppComponent appComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.application = application;
            this.appComponent = appComponent;
        }

        /**
         * 在你的 App 生命周期的任何阶段，{@link ComponentCallbacks2#onTrimMemory(int)} 发生的回调都预示着你设备的内存资源已经开始紧张
         * 你应该根据 {@link ComponentCallbacks2#onTrimMemory(int)} 发生回调时的内存级别来进一步决定释放哪些资源
         * {@link ComponentCallbacks2#onTrimMemory(int)} 的回调可以发生在 {@link Application}、{@link Activity}、{@link Service}、{@link ContentProvider}、{@link Fragment}
         *
         * @param level 内存级别
         * @see <a href="https://developer.android.com/reference/android/content/ComponentCallbacks2.html#TRIM_MEMORY_RUNNING_MODERATE">level 官方文档</a>
         */
        @Override
        public void onTrimMemory(int level) {
            // 状态1. 当开发者的 App 正在运行
            // 设备开始运行缓慢, 不会被 kill, 也不会被列为可杀死的, 但是设备此时正运行于低内存状态下, 系统开始触发杀死 LRU 列表中的进程的机制
            // case TRIM_MEMORY_RUNNING_MODERATE:

            // 设备运行更缓慢了, 不会被 kill, 但请你回收 unused 资源, 以便提升系统的性能, 你应该释放不用的资源用来提升系统性能 (但是这也会直接影响到你的 App 的性能)
            // case TRIM_MEMORY_RUNNING_LOW:

            // 设备运行特别慢, 当前 App 还不会被杀死, 但是系统已经把 LRU 列表中的大多数进程都已经杀死, 因此你应该立即释放所有非必须的资源
            // 如果系统不能回收到足够的 RAM 数量, 系统将会清除所有的 LRU 列表中的进程, 并且开始杀死那些之前被认为不应该杀死的进程, 例如那个包含了一个运行态 Service 的进程
            // case TRIM_MEMORY_RUNNING_CRITICAL:

            // 状态2. 当前 App UI 不再可见, 这是一个回收大个资源的好时机
            // case TRIM_MEMORY_UI_HIDDEN:

            // 状态3. 当前的 App 进程被置于 Background LRU 列表中
            // 进程位于 LRU 列表的上端, 尽管你的 App 进程并不是处于被杀掉的高危险状态, 但系统可能已经开始杀掉 LRU 列表中的其他进程了
            // 你应该释放那些容易恢复的资源, 以便于你的进程可以保留下来, 这样当用户回退到你的 App 的时候才能够迅速恢复
            // case TRIM_MEMORY_BACKGROUND:

            // 系统正运行于低内存状态并且你的进程已经已经接近 LRU 列表的中部位置, 如果系统的内存开始变得更加紧张, 你的进程是有可能被杀死的
            // case TRIM_MEMORY_MODERATE:
            
            // 系统正运行与低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
            // 低于 API 14 的 App 可以使用 onLowMemory 回调
            // case TRIM_MEMORY_COMPLETE:
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            // 系统正运行与低内存的状态并且你的进程正处于 LRU 列表中最容易被杀掉的位置, 你应该释放任何不影响你的 App 恢复状态的资源
        }
    }
}