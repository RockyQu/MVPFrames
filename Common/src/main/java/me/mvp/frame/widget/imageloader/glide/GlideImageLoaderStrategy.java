package me.mvp.frame.widget.imageloader.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.mvp.frame.widget.imageloader.BaseImageLoaderStrategy;
import me.mvp.frame.widget.imageloader.CacheStrategy;
import me.mvp.frame.widget.imageloader.ImageConfig;

/**
 * 最终的图片加截会执行到这里，此类只是简单的实现了 Glide 加载的策略，方便快速使用，但大部分情况会需
 * 要应对复杂的场这时可自行实现 {@link BaseImageLoaderStrategy} 和 {@link ImageConfig} 替换现有策略
 */
@Singleton
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<GlideConfig>, GlideAppliesOptions {

    @Inject
    public GlideImageLoaderStrategy() {
        ;
    }

    @SuppressLint("CheckResult")
    @Override
    public void load(Context context, GlideConfig config) {
        this.check(context, config);

        // 如果 Context 是 Activity 则自动使用 Activity 的生命周期
        GlideRequests requests = GlideFrames.with(context);

        // Url
        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl());

        // 缓存策略
        switch (config.getCacheStrategy()) {
            case CacheStrategy.ALL:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case CacheStrategy.NONE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case CacheStrategy.RESOURCE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case CacheStrategy.DATA:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case CacheStrategy.AUTOMATIC:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }

        if (config.isCrossFade()) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade());
        }

        if (config.isCenterCrop()) {
            glideRequest.centerCrop();
        }

        // 圆形
        if (config.isCircle()) {
            glideRequest.circleCrop();
        }

        // 圆角
        if (config.getRadius() > 0) {
            if (config.isCenterCrop()) {
                // 这里是为了解决圆角和 CenterCrop 属同时设置的冲突问题
                glideRequest.transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(config.getRadius())));
            } else {
                glideRequest.transform(new RoundedCorners(config.getRadius()));
            }
        }

        // 设置占位符
        if (config.getPlaceholder() != 0) {
            glideRequest.placeholder(config.getPlaceholder());
        }

        // 设置错误的图片
        if (config.getError() != 0) {
            glideRequest.error(config.getError());
        }

        // 设置请求 Url 为空图片
        if (config.getFallback() != 0) {
            glideRequest.fallback(config.getFallback());
        }

        glideRequest
                .into(config.getImageView());
    }

    @SuppressLint("CheckResult")
    @Override
    public void clear(final Context context, GlideConfig config) {
        if (context == null) throw new NullPointerException("Context is required");
        if (config == null) throw new NullPointerException("ImageConfigImpl is required");

        if (config.getImageView() != null) {// 取消正在执行的任务并且释放资源
            GlideFrames.get(context).getRequestManagerRetriever().get(context).clear(config.getImageView());
        }

        if (config.isClearDiskCache()) {// 清除本地缓存
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(context).clearDiskCache();
                        }
                    });
        }

        if (config.isClearMemory()) {// 清除内存缓存
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(context).clearMemory();
                        }
                    });
        }
    }

    /**
     * 合法性检查
     */
    private void check(Context context, GlideConfig config) {
        if (context == null) {
            throw new IllegalStateException("Context is required");
        }
        if (config == null) {
            throw new IllegalStateException("GlideImageConfig is required");
        }
        if (TextUtils.isEmpty(config.getUrl())) {
            throw new NullPointerException("Url is required");
        }
        if (config.getImageView() == null) {
            throw new NullPointerException("Imageview is required");
        }
    }

    @Override
    public void applyGlideOptions(Context context, GlideBuilder builder) {

    }
}