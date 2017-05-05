package com.tool.common.widget.imageloader.glide;

import android.app.Activity;
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

        RequestManager manager = null;
        if (context instanceof Activity) {
            manager = Glide.with((Activity) context);
        } else {
            manager = Glide.with(context);
        }

        DrawableRequestBuilder<String> requestBuilder = manager.load(config.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
                .crossFade()// 默认显示淡入淡出动画
                ;
//        if (config.getPlaceholder() != 0)//设置占位符
//            requestBuilder.placeholder(config.getPlaceholder());
//
//        if (config.getErrorPic() != 0)//设置错误的图片
//            requestBuilder.error(config.getErrorPic());

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