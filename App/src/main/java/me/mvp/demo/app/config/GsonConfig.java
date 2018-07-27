package me.mvp.demo.app.config;

import android.content.Context;

import com.google.gson.GsonBuilder;

import me.mvp.demo.app.utils.tool.GsonResponseDeserializer;
import me.mvp.frame.di.module.AppModule;
import me.mvp.demo.entity.base.ResponseEntity;

/**
 * 扩展自定义配置 Gson 参数
 */
public class GsonConfig implements AppModule.GsonConfiguration {

    @Override
    public void configGson(Context context, GsonBuilder builder) {
        builder
                .serializeNulls()// 支持序列化null的参数
                .registerTypeAdapter(ResponseEntity.class, new GsonResponseDeserializer());
    }
}