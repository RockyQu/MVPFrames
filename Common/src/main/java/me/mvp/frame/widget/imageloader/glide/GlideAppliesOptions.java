package me.mvp.frame.widget.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;

/**
 * 如果你想配置自己的Glide，请实现{@link me.mvp.frame.widget.imageloader.BaseImageLoader}
 * 的实现类也必须实现 {@link GlideAppliesOptions}
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