package me.mvp.frame.widget.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;

import me.mvp.frame.widget.imageloader.BaseImageLoaderStrategy;

/**
 * 如果你想配置自己的 Glide，请实现 {@link BaseImageLoaderStrategy}
 * 自定义的 Glide 实现类也必须实现 {@link GlideAppliesOptions}
 */
public interface GlideAppliesOptions {

    /**
     * 配置Glide的自定义参数
     *
     * @param context
     * @param builder {@link GlideBuilder} 此类被用来创建 Glide
     */
    void applyGlideOptions(Context context, GlideBuilder builder);
}