package com.frame.mvp.app;

import android.text.TextUtils;

import com.frame.mvp.BuildConfig;
import com.frame.mvp.app.api.Api;
import com.frame.mvp.entity.User;
import com.frame.mvp.mvp.login.LoginActivity;
import com.tool.common.base.AppConfiguration;
import com.tool.common.base.BaseApplication;
import com.tool.common.http.HttpCommunicationHandler;
import com.tool.common.http.ResponseEntity;
import com.tool.common.http.interceptor.LoggingInterceptor;
import com.tool.common.http.interceptor.NetworkInterceptor;
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
                .networkInterceptor(new NetworkInterceptor(new HttpCommunicationHandler() {

                    @Override
                    public Request onHttpRequest(Interceptor.Chain chain, Request request) {
                        return null;
                    }

                    @Override
                    public Response onHttpResponse(String result, Interceptor.Chain chain, Response response) {
                        //这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                        //重新请求token,并重新执行请求
                        if (!TextUtils.isEmpty(result)) {
                            ResponseEntity entity = GsonUtils.getEntity(result, ResponseEntity.class);
                        }

                        //这里如果发现token过期,可以先请求最新的token,然后在拿新的token放入request里去重新请求
                        //注意在这个回调之前已经调用过proceed,所以这里必须自己去建立网络请求,如使用okhttp使用新的request去请求
                        // create a new request and modify it accordingly using the new token
//                    Request newRequest = chain.request().newBuilder().header("token", newToken)
//                            .build();

//                    // retry the request
//
//                    response.body().close();
                        //如果使用okhttp将新的请求,请求成功后,将返回的response  return出去即可

                        //如果不需要返回新的结果,则直接把response参数返回出去
                        return response;
                    }
                }))
                .interceptors(new Interceptor[]{new LoggingInterceptor()})
                .imageLoader(new ImageLoader(new GlideImageLoader()))
                .build();
    }
}