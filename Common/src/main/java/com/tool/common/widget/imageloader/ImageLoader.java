package com.tool.common.widget.imageloader;

import android.content.Context;

/**
 * ImageLoader
 */
public class ImageLoader {

    private BaseImageLoader imageLoader;

    public ImageLoader(BaseImageLoader imageLoader) {
        setImageLoader(imageLoader);
    }

    public <T extends ImageConfig> void load(Context context, T config) {
        this.imageLoader.load(context, config);
    }

    public void setImageLoader(BaseImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
}