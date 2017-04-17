package com.frame.mvp.app;

import android.app.Application;
import android.content.Context;

import com.frame.mvp.BuildConfig;
import com.frame.mvp.app.api.Api;
import com.frame.mvp.di.common.component.AppComponent;
import com.frame.mvp.di.common.component.DaggerAppComponent;
import com.frame.mvp.di.common.module.DBModule;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.base.BaseApplication;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.di.module.ImageModule;
import com.tool.common.http.receiver.NetworkStatusReceiver;
import com.tool.common.integration.ConfigModule;
import com.tool.common.integration.ManifestParser;
import com.tool.common.log.QLog;
import com.tool.common.log.crash.ThreadCatchInterceptor;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;
import com.tool.common.utils.ProjectUtils;

import java.util.List;

/**
 * Application
 */
public class MVPApplication extends BaseApplication {

    // AppComponent
    private AppComponent appComponent;

    // LeakCanary观察器
    private RefWatcher watcher;

    // 当前登录用户信息
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        List<ConfigModule> modules = new ManifestParser(this).parse();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .httpModule(new HttpModule())
                .imageModule(new ImageModule())
                .appConfigModule(getAppConfigModule(this, modules))
                .dBModule(new DBModule())
                .build();

        appComponent.inject(this);

        for (ConfigModule module : modules) {
            module.registerComponents(this, appComponent.getRepositoryManager());
        }

        if (ProjectUtils.init()) {
            // 设置反馈崩溃信息，不需要可以不设置
            ThreadCatchInterceptor.getInstance().install(new ThreadCatchInterceptor.CallBack() {

                @Override
                public void error(Throwable throwable) {
                    ;
                }

                @Override
                public void finish(String path) {
                    QLog.i(path);
                }
            });
        } else {
            // 初始化失败
        }

        // 读取当前登录用户信息
        String value = PreferencesUtils.getString(this, LoginActivity.FLAG_USER, null);
        if (value != null) {
            user = GsonUtils.getEntity(value, User.class);
        }

        // LeakCanary内存泄露检查
        this.installLeakCanary();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    /**
     * 安装leakCanary检测内存泄露
     */
    protected void installLeakCanary() {
        this.watcher = BuildConfig.DEBUG ? LeakCanary.install(this) : RefWatcher.DISABLED;
    }

    /**
     * 获得leakCanary观察器
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        MVPApplication application = (MVPApplication) context.getApplicationContext();
        return application.watcher;
    }

    /**
     * 获得当前网络状态
     * <p>
     * NONE(1) 无网络，MOBILE(2)移动网络，WIFI(4)无线网络
     *
     * @return
     */
    public static NetworkStatusReceiver.Type getNetworkType() {
        return NetworkStatusReceiver.getType(getContext());
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private AppConfigModule getAppConfigModule(Application context, List<ConfigModule> modules) {
        AppConfigModule.Builder builder = AppConfigModule
                .builder()
                .httpUrl(Api.PHP);// 防止用户没有通过GlobeConfigModule配置，而导致报错，所以提前配置默认的BaseUrl

        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }

    @Override
    protected boolean logSwitch() {
        return BuildConfig.DEBUG;
    }

    /**
     * 返回AppComponent提供统一出口，AppComponent里拿到对象后都可以直接使用。
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (appComponent != null) {
            this.appComponent = null;
        }
        if (watcher != null) {
            this.watcher = null;
        }
    }
}