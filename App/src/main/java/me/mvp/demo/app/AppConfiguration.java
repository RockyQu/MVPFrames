package me.mvp.demo.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import io.rx_cache2.internal.RxCache;
import me.logg.Logg;
import me.logg.config.LoggConfiguration;
import me.mvp.demo.BuildConfig;
import me.mvp.demo.app.api.Api;
import me.mvp.demo.app.utils.gson.GsonResponseDeserializer;
import me.mvp.demo.entity.User;
import me.mvp.demo.mvp.login.LoginActivity;
import com.google.gson.GsonBuilder;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import me.mvp.frame.base.App;
import me.mvp.frame.base.delegate.ApplicationLifecycles;
import me.mvp.frame.di.module.AppConfigModule;
import me.mvp.frame.di.module.AppModule;
import me.mvp.frame.di.module.HttpModule;
import me.mvp.frame.http.NetworkHandler;
import me.mvp.frame.http.ResponseEntity;
import me.mvp.frame.http.cookie.PersistentCookieJar;
import me.mvp.frame.http.download.Downloader;
import me.mvp.frame.http.download.config.DownloaderConfiguration;
import me.mvp.frame.http.interceptor.LoggingInterceptor;
import me.mvp.frame.http.interceptor.ParameterInterceptor;
import me.mvp.frame.http.ssl.SSL;
import me.mvp.frame.http.ssl.TrustAllHostnameVerifier;
import me.mvp.frame.http.ssl.TrustAllX509TrustManager;
import me.mvp.frame.integration.ConfigModule;
import me.mvp.frame.utils.AppUtils;
import me.mvp.frame.utils.GsonUtils;
import me.mvp.frame.utils.PreferencesUtils;
import me.mvp.frame.utils.ProjectUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
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
////                                        Logg.e(resultToken);
////                                            Logg.e(resultToken.get("access_token"));
//                                        String token = resultToken.get("access_token").getAsString();
////                                            Logg.e(token);
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
                        builder.sslSocketFactory(SSL.createSSLSocketFactory(), new TrustAllX509TrustManager());
                        builder.hostnameVerifier(new TrustAllHostnameVerifier());
                    }
                })
                .rxCacheConfiguration(new HttpModule.RxCacheConfiguration() {
                    @Override
                    public RxCache configRxCache(Context context, RxCache.Builder builder) {
                        // 想自定义 RxCache 的缓存文件夹或者解析方式，如改成 fastjson，请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                        return null;
                    }
                })
                .gsonConfiguration(new AppModule.GsonConfiguration() {// 扩展自定义配置Gson参数
                    @Override
                    public void configGson(Context context, GsonBuilder builder) {
                        builder
                                .serializeNulls()// 支持序列化null的参数
                                .registerTypeAdapter(ResponseEntity.class, new GsonResponseDeserializer());
                    }
                })
                .cookieLoadForRequest(new PersistentCookieJar.CookieLoadForRequest() {

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                    }

                    @Override
                    public List<Cookie> loadForRequest(List<Cookie> cookies) {
                        return cookies;
                    }
                })
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
            public void attachBaseContext(Context base) {

            }

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