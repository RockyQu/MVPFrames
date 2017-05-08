package com.tool.common.widget.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tool.common.widget.imageloader.BaseImageLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * GlideImageLoader
 */
@Singleton
public class GlideImageLoader implements BaseImageLoader<GlideImageConfig> {

    @Inject
    public GlideImageLoader() {
        ;
    }

    @Override
    public void load(Context context, GlideImageConfig config) {
        this.check(context, config);

        RequestManager manager = Glide.with(context);

        DrawableRequestBuilder<String> requestBuilder = manager.load(config.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()// 默认显示淡入淡出动画
                ;

        // 缓存策略
        switch (config.getCacheStrategy()) {
            case 0:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
                break;
            case 3:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.RESULT);
                break;
        }

        // 改变图形的形状
        if (config.getTransformation() != null) {
            requestBuilder.transform(config.getTransformation());
        }

        // 设置占位符
        if (config.getPlaceholder() != 0) {
            requestBuilder.placeholder(config.getPlaceholder());
        }

        // 设置错误的图片
        if (config.getError() != 0) {
            requestBuilder.error(config.getError());
        }

        requestBuilder.into(config.getImageView());
    }

    @Override
    public void clear(Context context, GlideImageConfig config) {
        this.check(context, config);
    }

    /**
     * 合法性检查
     */
    private void check(Context context, GlideImageConfig config) {
        if (context == null) {
            throw new IllegalStateException("Context is required");
        }
        if (config == null) {
            throw new IllegalStateException("GlideImageConfig is required");
        }
    }
}