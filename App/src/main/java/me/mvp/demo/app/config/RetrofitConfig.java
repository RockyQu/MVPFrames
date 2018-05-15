package me.mvp.demo.app.config;

import android.content.Context;

import me.mvp.frame.di.module.HttpModule;
import retrofit2.Retrofit;

/**
 * 扩展自定义配置 Retrofit 参数
 */
public class RetrofitConfig implements HttpModule.RetrofitConfiguration {

    @Override
    public void configRetrofit(Context context, Retrofit.Builder builder) {

    }
}