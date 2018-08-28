package me.mvp.frame.widget.imageloader;

import android.content.Context;

/**
 * 图片加载策略，实现 {@link BaseImageLoaderStrategy}
 * 并通过 {@link ImageLoader#setImageLoader(BaseImageLoaderStrategy)} 配置后，才可进行图片请求
 */
public interface BaseImageLoaderStrategy<T extends ImageConfig> {

    /**
     * 加载图片
     *
     * @param context
     * @param config
     */
    void load(Context context, T config);

    /**
     * 停止加载
     *
     * @param context
     * @param config
     */
    void clear(Context context, T config);
}