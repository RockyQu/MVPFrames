package com.frame.mvp.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.frame.mvp.BuildConfig;
import com.frame.mvp.app.api.Api;
import com.frame.mvp.app.api.service.ApiCommon;
import com.frame.mvp.app.api.service.ApiUser;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tool.common.base.App;
import com.tool.common.base.delegate.AppDelegate;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.http.NetworkHandler;
import com.tool.common.http.interceptor.LoggingInterceptor;
import com.tool.common.http.interceptor.ParameterInterceptor;
import com.tool.common.integration.ConfigModule;
import com.tool.common.integration.IRepositoryManager;
import com.tool.common.log.log.LogConfig;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;
import com.tool.common.utils.ProjectUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * App全局配置信息在此配置，需要将此实现类声明到AndroidManifest中
 */
public class AppConfiguration implements ConfigModule {

    @Override
    public void applyOptions(final Context context, AppConfigModule.Builder builder) {
        builder
                .httpUrl(Api.PHP)
                .cacheFile(new File(ProjectUtils.CACHE))
                .networkHandler(new NetworkHandler() { // Http全局响应结果的处理类

                    @Override
                    public Request onHttpRequest(Interceptor.Chain chain, Request request) {
                        // 请求服务器之前可以拿到Request,做一些操作比如给Request统一添加Token或者Header，不做任务操作则直接返回Request
                        // return chain.request().newBuilder().header("token", tokenId).build();
                        return request;
                    }

                    @Override
                    public Response onHttpResponse(String result, Interceptor.Chain chain, Response response) {
                        // 这里提前拿到Http请求结果,可以用来检测，如Token是否过期
                        // if (!TextUtils.isEmpty(result)) {
                        //     ResponseEntity entity = GsonUtils.getEntity(result, ResponseEntity.class);
                        // }
                        // 如Token已过期，重新请求Token，然后拿新的Token放入Request重新继续完成上一次的请求
                        // 注意：在这个回调之前已经调用过Proceed,所以这里必须自己去建立网络请求,如使用OkHttp使用新的Request去请求
                        // Request newRequest = chain.request().newBuilder().header("token", newToken).build();
                        // response.body().close();
                        // 如果使用OkHttp将新的请求,请求成功后,将返回的Response  Return即可，如果不需要返回新的结果,则直接把Response参数返回出去
                        return response;
                    }
                })
                .interceptors(new Interceptor[]
                        {
                                new LoggingInterceptor(),
                                new ParameterInterceptor(new ParameterInterceptor.ParameterCallback() {

                                    /**
                                     * 这里为接口添加类型为HashMap的统一参数，例如Token、版本号等。支持Get、Post方法，ParameterInterceptor会自动判断
                                     */
                                    @Override
                                    public HashMap<String, String> parameters() {
                                        User user = (User) ((App) context).getAppComponent().extras().get(LoginActivity.class.getName());

                                        HashMap<String, String> parameters = new HashMap<>();
                                        if (user != null) {
                                            // 为接口统一添加access_token参数
                                            // parameters.put("access_token", user.getToken());
                                        }

                                        return parameters;
                                    }
                                })
                        });
    }

    @Override
    public void registerComponents(Context context, IRepositoryManager repositoryManager) {
        repositoryManager.injectApiService(ApiCommon.class, ApiUser.class);
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycleManager) {
        // AppDelegateManager.Lifecycle的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycleManager.add(new AppDelegate.Lifecycle() {

            @Override
            public void onCreate(Application application) {

                // Log
                if (BuildConfig.DEBUG_FLAG) {
                    LogConfig.Buidler
                            .buidler()
                            .setContext(application)
                            .setOpen(BuildConfig.DEBUG_FLAG)
                            .build();
                }

                // LeakCanary内存泄露检查
                ((App) application).getAppComponent().extras().put(RefWatcher.class.getName(), BuildConfig.DEBUG_FLAG ? LeakCanary.install(application) : RefWatcher.DISABLED);
            }

            @Override
            public void onTerminate(Application application) {

            }
        });

        lifecycleManager.add(new AppDelegate.Lifecycle() {

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
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {

    }
}