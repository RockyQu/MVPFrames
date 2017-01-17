package com.tool.common.widget.imageloader;

import android.content.Context;

/**
 * BaseImageLoader
 */
public interface BaseImageLoader<T extends ImageConfig> {

    void load(Context ctx, T config);
}