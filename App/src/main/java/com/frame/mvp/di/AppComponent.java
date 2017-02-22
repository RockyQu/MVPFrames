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

    // 图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader getImageLoader();
}