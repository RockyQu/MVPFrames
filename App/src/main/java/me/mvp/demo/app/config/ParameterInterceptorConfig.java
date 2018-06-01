package me.mvp.demo.app.config;

import android.content.Context;

import java.util.HashMap;

import me.mvp.demo.entity.User;
import me.mvp.demo.mvp.login.LoginActivity;
import me.mvp.frame.base.App;
import me.mvp.frame.http.interceptor.ParameterInterceptor;

/**
 * 这里为接口添加类型为HashMap的统一参数，例如Token、版本号等。支持Get、Post方法，ParameterInterceptor会自动判断
 */
public class ParameterInterceptorConfig extends ParameterInterceptor {

    private Context context;

    public ParameterInterceptorConfig(Context context) {
        this.context = context;
    }

    @Override
    public HashMap<String, Object> parameters() {
//        User user = (User) ((App) context).getAppComponent().extras().get(LoginActivity.class.getName());
//
//        HashMap<String, Object> parameters = new HashMap<>();
//        if (user != null) {
//            // 为接口统一添加access_token参数
//            parameters.put("access_token", user.getToken());
//        }
        return null;
    }
}
