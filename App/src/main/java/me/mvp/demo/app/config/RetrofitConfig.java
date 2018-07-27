package me.mvp.demo.app.config;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Inject;

import me.mvp.demo.app.utils.tool.GsonConverterBodyFactory;
import me.mvp.demo.app.utils.tool.JsonConverterFactory;
import me.mvp.frame.di.module.HttpModule;
import retrofit2.Retrofit;

/**
 * 扩展自定义配置 Retrofit 参数
 */
public class RetrofitConfig implements HttpModule.RetrofitConfiguration {

    @Inject
    Gson gson;

    @Override
    public void configRetrofit(Context context, Retrofit.Builder builder) {
        builder.addConverterFactory(GsonConverterBodyFactory.create(gson))// 支持 Gson
                .addConverterFactory(JsonConverterFactory.create())// 支持 Json
        ;
    }
}