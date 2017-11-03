package com.frame.mvp.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.frame.mvp.BuildConfig;
import com.frame.mvp.app.api.Api;
import com.frame.mvp.db.DBModule;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.google.gson.GsonBuilder;
import com.logg.Logg;
import com.logg.config.LoggConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tool.common.base.App;
import com.tool.common.base.delegate.ApplicationLifecycles;
import com.tool.common.di.module.AppConfigModule;
import com.tool.common.di.module.AppModule;
import com.tool.common.di.module.HttpModule;
import com.tool.common.http.NetworkHandler;
import com.tool.common.http.download.Downloader;
import com.tool.common.http.download.config.DownloaderConfiguration;
import com.tool.common.http.interceptor.LoggingInterceptor;
import com.tool.common.http.interceptor.ParameterInterceptor;
import com.tool.common.integration.ConfigModule;
import com.tool.common.utils.AppUtils;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;
import com.tool.common.utils.ProjectUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * App全局配置信息在此配置，需要将此实现类声明到AndroidManifest中
 */
public class AppConfiguration implements ConfigModule {

    @Override
    public void applyOptions(final Context context, AppConfigModule.Builder builder) {
        builder
                .httpUrl(Api.APP_DOMAIN)
                .cacheFile(new File(ProjectUtils.CACHE))
                .networkHandler(new NetworkHandler() { // Http全局响应结果的处理类

                    @Override
                    public Request onHttpRequest(Interceptor.Chain chain, Request request) {
                        // 请求服务器之前可以拿到Request,做一些操作比如给Request统一添加Token或者Header，不做任务操作则直接返回Request
                        // return chain.request().newBuilder().header("token", tokenId).build();
                        return request;
                    }

                    @Override
                    public Response onHttpResponse(String result, Interceptor.Chain chain, Request request, Response response) {
                        // 这里提前拿到Http请求结果,可以用来检测，如Token是否过期
//                        if (!TextUtils.isEmpty(result)) {
//                            ResponseEntity entity = GsonUtils.getEntity(result, ResponseEntity.class);
//
//                            // 登录超时或授权失效，重新获取授权
//                            if (entity.getCode() == -253 || entity.getCode() == -254) {
//                                AppComponent component = ((App) context).getAppComponent();
//
//                                // 读取当前登录用户信息
//                                String value = PreferencesUtils.getString(context, LoginActivity.class.getName(), null);
//                                if (value != null) {
//                                    User user = GsonUtils.getEntity(value, User.class);
//
//                                    Call<ResponseEntity<JsonObject>> apiToken = component.getRepositoryManager().obtainApiService(ApiCommon.class).getToken("CF2QCXW6", DeviceUtils.getIMEI(context), user.getUserId());
//                                    try {
//                                        JsonObject resultToken = apiToken.execute().body().getData();
//                                        apiToken.execute().body();
////                                        QLog.e(resultToken);
////                                            QLog.e(resultToken.get("access_token"));
//                                        String token = resultToken.get("access_token").getAsString();
////                                            QLog.e(token);
//
//                                        HttpUrl.Builder modifiedUrl = request.url().newBuilder()
//                                                .scheme(request.url().scheme())
//                                                .host(request.url().host());
//
//                                        modifiedUrl.addQueryParameter("access_token", token);
//
//                                        Request newRequest = request.newBuilder()
////                                                .method(request.method(), request.body())
////                                                .url(modifiedUrl.build())
//                                                .build();
//                                        response.body().close();
//                                        return chain.proceed(newRequest);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }

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
                                    public HashMap<String, Object> parameters() {
                                        User user = (User) ((App) context).getAppComponent().extras().get(LoginActivity.class.getName());

                                        HashMap<String, Object> parameters = new HashMap<>();
                                        if (user != null) {
                                            // 为接口统一添加access_token参数
                                            parameters.put("access_token", user.getToken());
                                        }

                                        return parameters;
                                    }
                                })
                        })
                .retrofitConfiguration(new HttpModule.RetrofitConfiguration() {// 扩展自定义配置Retrofit参数

                    @Override
                    public void configRetrofit(Context context, Retrofit.Builder builder) {

                    }
                })
                .okHttpConfiguration(new HttpModule.OkHttpConfiguration() {// 扩展自定义配置OkHttp参数
                    @Override
                    public void configOkHttp(Context context, OkHttpClient.Builder builder) {
                        // builder.sslSocketFactory(SSL.createSSLSocketFactory(),new TrustAllX509TrustManager());
                        // builder.hostnameVerifier(new TrustAllHostnameVerifier());
                    }
                })
                .gsonConfiguration(new AppModule.GsonConfiguration() {// 扩展自定义配置Gson参数
                    @Override
                    public void configGson(Context context, GsonBuilder builder) {
                        builder
                                .serializeNulls();// 支持序列化null的参数
                    }
                })
        ;
    }

    @Override
    public void injectAppLifecycle(Context context, List<ApplicationLifecycles> lifecycleManager) {
        // AppDelegateManager.Lifecycle的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycleManager.add(new ApplicationLifecycles() {

            @Override
            public void onCreate(Application application) {
                LoggConfiguration configuration = new LoggConfiguration.Buidler()
                        .setDebug(BuildConfig.DEBUG_FLAG)
//                .setTag("test")// 自定义全局Tag
                        .build();
                Logg.init(configuration);

                // LeakCanary内存泄露检查
                ((App) application).getAppComponent().extras().put(RefWatcher.class.getName(), BuildConfig.DEBUG_FLAG ? LeakCanary.install(application) : RefWatcher.DISABLED);
            }

            @Override
            public void onTerminate(Application application) {

            }
        });

        lifecycleManager.add(new ApplicationLifecycles() {

            // 渠道名称，必须与Mainfests渠道配置name相同
            final String CHANNEL = "Channel";

            @Override
            public void onCreate(Application application) {
                // 项目在SDCard下创建的目录
                if (!ProjectUtils.init(AppUtils.getAppChannel(application, CHANNEL))) {
                    // 失败
                }
            }

            @Override
            public void onTerminate(Application application) {

            }
        });

        lifecycleManager.add(new ApplicationLifecycles() {

            @Override
            public void onCreate(Application application) {
                // 数据库
                DBModule.getInstance().init(application);
            }

            @Override
            public void onTerminate(Application application) {

            }
        });
        
        lifecycleManager.add(new ApplicationLifecycles() {

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
            public void onCreate(Application application) {
                // 这是一个轻量级下载模块，使用前请在这里调用如下代码
                DownloaderConfiguration configuration = DownloaderConfiguration.builder()
                        .application(application)
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