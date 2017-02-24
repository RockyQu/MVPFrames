package com.frame.mvp.app;

import android.content.Context;

import com.frame.mvp.BuildConfig;
import com.frame.mvp.app.api.Api;
import com.frame.mvp.di.AppComponent;
import com.frame.mvp.di.DaggerAppComponent;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tool.common.base.AppConfiguration;
import com.tool.common.base.BaseApplication;
import com.tool.common.http.NetworkHandler;
import com.tool.common.http.interceptor.LoggingInterceptor;
import com.tool.common.http.interceptor.NetworkInterceptor;
import com.tool.common.http.interceptor.ParameterInterceptor;
import com.tool.common.log.QLog;
import com.tool.common.log.crash.ThreadCatchInterceptor;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;
import com.tool.common.utils.ProjectUtils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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

        appComponent = DaggerAppComponent
                .builder()
                .appConfiguration(getAppConfiguration())
                .appModule(getAppModule())
                .httpModule(getHttpModule())
                .imageModule(getImageModule())
                .build();

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

        if (ProjectUtils.init()) {

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

    @Override
    protected AppConfiguration getAppConfiguration() {
        return AppConfiguration.Buidler.buidler()
                .httpUrl(Api.PHP)
                .httpCacheFile(this.getCacheDir())
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
                        // 如果使用OkHttp将新的请求,请求成功后,将返回的Response  Return即可，如果不需要返回新的结果,则直接把response参数返回出去
                        return response;
                    }
                })
//                .interceptors(new Interceptor[]
//                        {
//                                new LoggingInterceptor(),
//                                new ParameterInterceptor()
//                        })
                .build();
    }

    /**
     * 返回AppComponent提供统一出口，AppComponent里拿到对象后都可以直接使用。
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }
}