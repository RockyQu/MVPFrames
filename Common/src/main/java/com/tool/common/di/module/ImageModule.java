package com.tool.common.di.module;

import com.tool.common.widget.imageloader.BaseImageLoader;
import com.tool.common.widget.imageloader.ImageLoader;
import com.tool.common.widget.imageloader.glide.GlideImageLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ImageModule
 */
@Module
public class ImageModule {

    @Singleton
    @Provides
    public BaseImageLoader provideImageLoader(GlideImageLoader imageLoader) {
        return imageLoader;
    }
}