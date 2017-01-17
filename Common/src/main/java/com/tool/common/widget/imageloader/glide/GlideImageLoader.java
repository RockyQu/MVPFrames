package com.tool.common.widget.imageloader.glide;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tool.common.widget.imageloader.BaseImageLoader;

/**
 * GlideImageLoader
 */
public class GlideImageLoader implements BaseImageLoader<GlideImageConfig> {

    public GlideImageLoader() {
        ;
    }

    @Override
    public void load(Context ctx, GlideImageConfig config) {
        RequestManager manager = null;
        if (ctx instanceof Activity)//如果是activity则可以使用Activity的生命周期
            manager = Glide.with((Activity) ctx);
        else
            manager = Glide.with(ctx);

        DrawableRequestBuilder<String> requestBuilder = manager.load(config.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop();
//        if (config.getPlaceholder() != 0)//设置占位符
//            requestBuilder.placeholder(config.getPlaceholder());
//
//        if (config.getErrorPic() != 0)//设置错误的图片
//            requestBuilder.error(config.getErrorPic());

        requestBuilder.into(config.getImageView());
    }
}