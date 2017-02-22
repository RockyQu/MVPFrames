package com.frame.mvp.di;

import com.tool.common.widget.imageloader.ImageLoader;
import com.tool.common.di.module.ImageModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * AppComponent
 */
@Singleton
@Component(modules = {ImageModule.class})
public interface AppComponent {

    // Picture Manager
    ImageLoader getImageLoader();
}