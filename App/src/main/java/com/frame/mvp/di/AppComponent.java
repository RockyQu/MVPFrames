package com.frame.mvp.di;

import android.app.Application;

import com.google.gson.Gson;
import com.tool.common.di.module.AppModule;
import com.tool.common.widget.imageloader.ImageLoader;
import com.tool.common.di.module.ImageModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * AppComponent
 */
@Singleton
@Component(modules = {AppModule.class,ImageModule.class})
public interface AppComponent {

    // Application
    Application getApplication();

    // Gson
    Gson getGson();

    // Picture Manager
    ImageLoader getImageLoader();
}