package me.mvp.frame.widget.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import me.mvp.frame.widget.imageloader.ImageConfig;

/**
 * 图片加载的配置信息，可以根据需要自定义
 */
public class GlideConfig extends ImageConfig {

    // 缓存策略，0=DiskCacheStrategy.all，1=DiskCacheStrategy.NONE，2=DiskCacheStrategy.SOURCE，3=DiskCacheStrategy.RESULT
    private int cacheStrategy;
    // 请求 Url 为空，则使用此图片作为占位符
    private int fallback;
    // 改变图形的形状
    private BitmapTransformation transformation;

    // 图像加载模式
    private ImageScaleType scaleType;

    // 是否使用淡入淡出过渡动画
    private boolean crossFade;
    // 是否移动所有加载动画，注意如果加载 Gif 会导致加载失败
    private boolean dontAnimate;

    // 清理内存缓存
    private boolean clearMemory;
    // 清理本地缓存
    private boolean clearDiskCache;

    private GlideConfig(Buidler builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.error = builder.error;

        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.fallback = builder.fallback;

        this.scaleType = builder.scaleType;

        this.crossFade = builder.crossFade;
        this.dontAnimate = builder.dontAnimate;

        this.clearMemory = builder.clearMemory;
        this.clearDiskCache = builder.clearDiskCache;
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

    public ImageScaleType getScaleType() {
        return scaleType;
    }

    public boolean isCrossFade() {
        return crossFade;
    }

    public boolean isDontAnimate() {
        return dontAnimate;
    }

    public boolean isClearMemory() {
        return clearMemory;
    }

    public boolean isClearDiskCache() {
        return clearDiskCache;
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

        private ImageScaleType scaleType;

        private boolean crossFade;
        private boolean dontAnimate = false;

        private boolean clearMemory;
        private boolean clearDiskCache;

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

        public Buidler scaleType(ImageScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Buidler crossFade(boolean crossFade) {
            this.crossFade = crossFade;
            return this;
        }

        public Buidler dontAnimate() {
            this.dontAnimate = true;
            return this;
        }

        public Buidler clearMemory(boolean clearMemory) {
            this.clearMemory = clearMemory;
            return this;
        }

        public Buidler clearDiskCache(boolean clearDiskCache) {
            this.clearDiskCache = clearDiskCache;
            return this;
        }

        public GlideConfig build() {
            return new GlideConfig(this);
        }
    }
}