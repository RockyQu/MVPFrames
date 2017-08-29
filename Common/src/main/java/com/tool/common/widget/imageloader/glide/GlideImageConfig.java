package com.tool.common.widget.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.tool.common.widget.imageloader.ImageConfig;

/**
 * 图片加载的配置信息，可以根据需要自定义
 */
public class GlideImageConfig extends ImageConfig {

    // 缓存策略，0=DiskCacheStrategy.all，1=DiskCacheStrategy.NONE，2=DiskCacheStrategy.SOURCE，3=DiskCacheStrategy.RESULT
    private int cacheStrategy;
    //请求Url为空，则使用此图片作为占位符
    private int fallback;
    // 改变图形的形状
    private BitmapTransformation transformation;
    // 清理内存缓存
    private boolean isClearMemory;
    // 清理本地缓存
    private boolean isClearDiskCache;

    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.error = builder.error;

        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.fallback = builder.fallback;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    public int getCacheStrategy() {
        return cacheStrategy;
    }

    public int getFallback() {
        return fallback;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public boolean isClearMemory() {
        return isClearMemory;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public static Buidler builder() {
        return new Buidler();
    }

    public static final class Buidler {

        private String url;
        private ImageView imageView;
        private int placeholder;
        private int error;

        private int cacheStrategy;
        private int fallback;
        private BitmapTransformation transformation;
        private boolean isClearMemory;
        private boolean isClearDiskCache;

        private Buidler() {
            ;
        }

        public Buidler url(String url) {
            this.url = url;
            return this;
        }

        public Buidler imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Buidler placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Buidler error(int error) {
            this.error = error;
            return this;
        }

        public Buidler cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Buidler fallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public Buidler transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Buidler isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public Buidler isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public GlideImageConfig build() {
            return new GlideImageConfig(this);
        }
    }
}