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
import com.bumptech.glide.module.GlideModule;
import com.tool.common.base.BaseApplication;
import com.tool.common.log.QLog;
import com.tool.common.utils.FileUtils;

import java.io.File;

/**
 * Glide全局配置
 */
public class GlideConfiguration implements GlideModule {

    // 图片缓存文件最大值为100MB
    private static final int DISK_CACHE_MAX_SIZE = 1024 * 1024 * 100;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 自定义缓存目录
//        builder.setDiskCache(new DiskCache.Factory() {
//
//            @Override
//            public DiskCache build() {
//                // Careful: the external cache directory doesn't enforce permissions
//                AppComponent appComponent = ((BaseApplication)BaseApplication.getContext().getApplicationContext()).getAppComponent();
//                return DiskLruCacheWrapper.get(FileUtils.makeDirs(new File(appComponent.cacheFile(), "Glide")), IMAGE_DISK_CACHE_MAX_SIZE);
//                return DiskLruCacheWrapper.get(FileUtils.getCacheFile(BaseApplication.getContext()), DISK_CACHE_MAX_SIZE);
//            }
//        });



        // 自定义内存缓存和图片池大小
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int customMemoryCacheSize = (int) (1.2 * calculator.getMemoryCacheSize());
        int customBitmapPoolSize = (int) (1.2 * calculator.getBitmapPoolSize());

        // 设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        // 设置缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        // 图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        QLog.e("测试registerComponents");
    }
}
