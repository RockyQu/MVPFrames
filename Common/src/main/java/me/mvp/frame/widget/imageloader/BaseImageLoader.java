package me.mvp.frame.widget.imageloader;

import android.content.Context;

/**
 * BaseImageLoader
 */
public interface BaseImageLoader<T extends ImageConfig> {

    void load(Context context, T config);

    void clear(Context context, T config);
}