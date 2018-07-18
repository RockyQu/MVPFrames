package me.mvp.demo.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import me.mvp.demo.BuildConfig;
import me.mvp.demo.app.api.Api;
import me.mvp.demo.app.config.DBConfig;
import me.mvp.demo.app.config.GlobalErrorConfig;
import me.mvp.demo.app.config.GsonConfig;
import me.mvp.demo.app.config.NetworkInterceptorConfig;
import me.mvp.demo.app.config.OkHttpConfig;
import me.mvp.demo.app.config.ParameterInterceptorConfig;
import me.mvp.demo.app.config.RetrofitConfig;
import me.mvp.demo.app.config.RxCacheConfig;
import me.mvp.demo.entity.User;
import me.mvp.demo.mvp.login.LoginActivity;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import me.mvp.frame.base.App;
import me.mvp.frame.base.delegate.ApplicationLifecycles;
import me.mvp.frame.di.module.AppConfigModule;
import me.mvp.frame.http.download.Downloader;
import me.mvp.frame.http.download.config.DownloaderConfiguration;
import me.mvp.frame.http.interceptor.NetworkInterceptor;
import me.mvp.frame.integration.ConfigModule;
import me.mvp.frame.utils.AppUtils;
import me.mvp.frame.utils.GsonUtils;
import me.mvp.frame.utils.PreferencesUtils;
import me.mvp.frame.utils.ProjectUtils;

import java.io.File;
import java.util.List;

/**
 * 全局配置信息在此配置，需要将此实现类声明到 AndroidManifest 中
 */
public class AppConfiguration implements ConfigModule {

    // 渠道名称，必须与Mainfests渠道配置name相同
    public static final String CHANNEL = "Channel";

    @Override
    public void applyOptions(final Context context, AppConfigModule.Builder builder) {
        if (BuildConfig.DEBUG_FLAG) { // 只在 Debug 时打印日志
            builder.httpLogLevel(NetworkInterceptor.Level.ALL);
        } else {
            builder.httpLogLevel(NetworkInterceptor.Level.NONE);
        }

        builder
                .httpUrl(Api.APP_DOMAIN)
                .cacheFile(new File(ProjectUtils.CACHE))
                .dbConfiguration(new DBConfig())
                .networkInterceptorHandler(new NetworkInterceptorConfig())
                .interceptors(new ParameterInterceptorConfig(context))
                .retrofitConfiguration(new RetrofitConfig())
                .okHttpConfiguration(new OkHttpConfig())
                .globalErrorHandler(new GlobalErrorConfig())
                .rxCacheConfiguration(new RxCacheConfig())
                .gsonConfiguration(new GsonConfig())
        ;
    }

    @Override
    public void injectApplicationLifecycle(Context context, List<ApplicationLifecycles> lifecycleManager) {
        lifecycleManager.add(new ApplicationLifecycles() {

            @Override
            public void attachBaseContext(Context base) {

            }

            @Override
            public void onCreate(Application application) {
                // LeakCanary内存泄露检查
                ((App) application).getAppComponent().extras().put(RefWatcher.class.getName(), BuildConfig.DEBUG_FLAG ? LeakCanary.install(application) : RefWatcher.DISABLED);
            }

            @Override
            public void onTerminate(Application application) {

            }
        });

        lifecycleManager.add(new ApplicationLifecycles() {

            @Override
            public void attachBaseContext(Context base) {

            }

            @Override
            public void onCreate(Application application) {
                // 读取当前登录用户信息
                String value = PreferencesUtils.getString(application, LoginActivity.class.getName(), null);
                if (value != null) {
                    User user = GsonUtils.getEntity(value, User.class);
                    ((App) application).getAppComponent().extras().put(LoginActivity.class.getName(), user);
                }
            }

            @Override
            public void onTerminate(Application application) {

            }
        });

        lifecycleManager.add(new ApplicationLifecycles() {

            @Override
            public void attachBaseContext(Context base) {

            }

            @Override
            public void onCreate(Application application) {
                DownloaderConfiguration configuration = DownloaderConfiguration.builder()
                        .application(application)
                        .debug(BuildConfig.DEBUG_FLAG)
                        .build();
                Downloader.getInstance().init(configuration);
            }

            @Override
            public void onTerminate(Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) ((App) f.getActivity().getApplication()).getAppComponent().extras().get(RefWatcher.class.getName())).watch(this);
            }
        });
    }
}