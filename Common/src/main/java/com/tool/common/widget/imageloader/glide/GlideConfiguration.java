package com.tool.common.widget.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.tool.common.base.App;
import com.tool.common.di.component.AppComponent;
import com.tool.common.http.OkHttpUrlLoader;
import com.tool.common.utils.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Glide全局配置
 */
public class GlideConfiguration implements GlideModule {

    // 缓存文件最大值为100MB
    private static final int DISK_SIZE = 1024 * 1024 * 100;

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {

        // 缓存目录
        builder.setDiskCache(new DiskCache.Factory() {

            @Override
            public DiskCache build() {
                AppComponent component = ((App) context.getApplicationContext()).getAppComponent();
                return DiskLruCacheWrapper.get(FileUtils.makeDirs(new File(component.cacheFile(), "Glide")), DISK_SIZE);
            }
        });

        // 自定义内存缓存和图片池大小
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        builder.setMemoryCache(new LruResourceCache((int) (1.2 * calculator.getMemoryCacheSize())));
        builder.setBitmapPool(new LruBitmapPool((int) (1.2 * calculator.getBitmapPoolSize())));

        // 图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // 将Glide默认使用HttpURLConnection的网络请求切换成okhttp请求
        AppComponent component = ((App) context.getApplicationContext()).getAppComponent();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(component.getOkHttpClient()));
    }
}
