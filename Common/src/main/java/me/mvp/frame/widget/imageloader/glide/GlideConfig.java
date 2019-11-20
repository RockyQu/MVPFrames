package me.mvp.frame.widget.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import me.mvp.frame.widget.imageloader.CacheStrategy;
import me.mvp.frame.widget.imageloader.ImageConfig;

/**
 * 图片加载的配置信息，可以根据需要自定义
 */
public class GlideConfig extends ImageConfig {

    // 图片的缓存策略
    @CacheStrategy.Strategy
    private int cacheStrategy;

    // 请求 Url 为空时使用此图片作为占位符
    private int fallback;

    // 圆角的大小
    private int radius;

    // 同时加载多个 ImageView
    private ImageView[] imageViews;

    // 是否使用淡入淡出过渡动画
    private boolean crossFade;
    // 是否将图片剪切为 CenterCrop
    private boolean centerCrop;
    // 是否将图片剪切为圆形
    private boolean circle;

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
        this.fallback = builder.fallback;
        this.radius = builder.radius;
        this.imageViews = builder.imageViews;
        this.crossFade = builder.crossFade;
        this.centerCrop = builder.centerCrop;
        this.circle = builder.circle;

        this.clearMemory = builder.clearMemory;
        this.clearDiskCache = builder.clearDiskCache;
    }

    public int getCacheStrategy() {
        return cacheStrategy;
    }

    public int getFallback() {
        return fallback;
    }

    public int getRadius() {
        return radius;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public boolean isCrossFade() {
        return crossFade;
    }

    public boolean isCenterCrop() {
        return centerCrop;
    }

    public boolean isCircle() {
        return circle;
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

        private int radius;

        private ImageView[] imageViews;

        private boolean crossFade;
        private boolean centerCrop;
        private boolean circle;

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

        public Buidler cacheStrategy(@CacheStrategy.Strategy int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Buidler fallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public Buidler radius(int radius) {
            this.radius = radius;
            return this;
        }

        public Buidler imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public Buidler crossFade(boolean crossFade) {
            this.crossFade = crossFade;
            return this;
        }

        public Buidler centerCrop(boolean centerCrop) {
            this.centerCrop = centerCrop;
            return this;
        }

        public Buidler circle(boolean circle) {
            this.circle = circle;
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