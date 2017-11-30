package com.tool.common.widget.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.logg.Logg;
import com.tool.common.widget.imageloader.BaseImageLoader;
import com.tool.common.widget.imageloader.utils.ImageScaleType;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * GlideImageLoader
 */
@Singleton
public class GlideImageLoader implements BaseImageLoader<GlideImageConfig>, GlideAppliesOptions {

    @Inject
    public GlideImageLoader() {
        ;
    }

    @Override
    public void load(Context context, GlideImageConfig config) {
        this.check(context, config);

        // 如果Context是Activity则自动使用Activity的生命周期
        GlideRequests requests = GlideFrames.with(context);

        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade());

        // 缓存策略
        switch (config.getCacheStrategy()) {
            case 0:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case 3:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case 4:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
        }

        ImageScaleType scaleType = config.getScaleType();
        if (scaleType != null) {
            switch (scaleType) {
                case CENTER_CROP:
                    glideRequest.centerCrop();
                    break;
                case CENTER_INSIDE:
                    glideRequest.centerInside();
                    break;
                case FIT_CENTER:
                    glideRequest.fitCenter();
                    break;
                default:
                    break;
            }
        }
        
        if (config.getTransformation() != null) {//glide用它来改变图形的形状
            glideRequest.transform(config.getTransformation());
        }

        // 设置占位符
        if (config.getPlaceholder() != 0) {
            glideRequest.placeholder(config.getPlaceholder());
        }

        // 设置错误的图片
        if (config.getError() != 0) {
            glideRequest.error(config.getError());
        }

        // 设置请求 url 为空图片
        if (config.getFallback() != 0) {
            glideRequest.fallback(config.getFallback());
        }

        glideRequest
                .into(config.getImageView());
    }

    @Override
    public void clear(final Context context, GlideImageConfig config) {
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
    private void check(Context context, GlideImageConfig config) {
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
        Logg.w("applyGlideOptions");
    }
}