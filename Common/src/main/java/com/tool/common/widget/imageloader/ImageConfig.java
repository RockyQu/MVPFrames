package com.tool.common.widget.imageloader;

import android.widget.ImageView;

/**
 * 图片加载配置信息
 */
public class ImageConfig {

    // 加截URL
    protected String url;
    // ImageView
    protected ImageView imageView;
    // 占位符
    protected int placeholder;
    // 加截错误
    protected int error;

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getError() {
        return error;
    }
}