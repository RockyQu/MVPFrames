package com.frame.mvp.app;

import com.frame.mvp.BuildConfig;
import com.frame.mvp.app.api.Api;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.tool.common.base.AppConfiguration;
import com.tool.common.base.BaseApplication;
import com.tool.common.http.interceptor.LoggingInterceptor;
import com.tool.common.http.interceptor.NetworkInterceptor;
import com.tool.common.http.interceptor.ParameterInterceptor;
import com.tool.common.log.common.Setting;
import com.tool.common.utils.GsonUtils;
import com.tool.common.utils.PreferencesUtils;
import com.tool.common.utils.ProjectUtils;
import com.tool.common.widget.imageloader.ImageLoader;
import com.tool.common.widget.imageloader.glide.GlideImageLoader;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Application
 */
public class MVPApplication extends BaseApplication {

    // 当前登录用户信息
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        Setting.getInstance()
                .setContext(getApplicationContext())
                .setTag(null)// 设置LogTag
                .setDebug(appConfiguration.isDebug());// 开启Log输出

        if (!ProjectUtils.init()) {

        }

        // 读取当前登录用户信息
        String value = PreferencesUtils.getString(this, LoginActivity.FLAG_USER, null);
        if (value != null) {
            user = GsonUtils.getEntity(value, User.class);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    protected AppConfiguration getAppConfiguration() {
        return AppConfiguration.Buidler.buidler()
                .debug(BuildConfig.DEBUG)
                .httpUrl(Api.PHP)
                .httpCacheFile(this.getCacheDir())
                .networkInterceptor(new NetworkInterceptor(new NetworkInterceptor.NetworkCallback() { // Http全局响应结果的处理类

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
                }))
                .interceptors(new Interceptor[]
                        {
                                new LoggingInterceptor(),
                                new ParameterInterceptor()
                        })
                .imageLoader(new ImageLoader(new GlideImageLoader()))
                .build();
    }
}