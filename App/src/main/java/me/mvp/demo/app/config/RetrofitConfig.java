package me.mvp.demo.app.config;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Inject;

import me.logg.Logg;
import me.mvp.demo.app.utils.tool.GsonConverterBodyFactory;
import me.mvp.demo.app.utils.tool.JsonConverterFactory;
import me.mvp.frame.di.module.HttpModule;
import retrofit2.Retrofit;

/**
 * 扩展自定义配置 Retrofit 参数
 */
public class RetrofitConfig implements HttpModule.RetrofitConfiguration {

    @Override
    public void configRetrofit(Context context, Retrofit.Builder builder) {
        builder
                .addConverterFactory(JsonConverterFactory.create())// 支持 Json
        ;
    }
}