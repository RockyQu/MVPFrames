package me.mvp.frame.widget.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;
import java.io.InputStream;

import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.http.OkHttpUrlLoader;
import me.mvp.frame.utils.AppUtils;
import me.mvp.frame.utils.FileUtils;
import me.mvp.frame.widget.imageloader.BaseImageLoaderStrategy;

/**
 * Glide 全局配置
 */
@GlideModule(glideName = "GlideFrames")
public class GlideConfigModule extends AppGlideModule {

    // 图片缓存文件最大值为100MB
    public static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        final AppComponent component = AppUtils.Companion.obtainAppComponentFromContext(context);
        builder.setDiskCache(new DiskCache.Factory() {

            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                File file = new File(component.getCacheFile(), "Glide");
                FileUtils.createOrExistsDir(file);
                return DiskLruCacheWrapper.create(file, IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        // 图片框架最终会走到 GlideImageLoader 的 load 方法中进行处理
        // 如果你想自己自定义图片框架或自己实现 Glide BaseImageLoader 你需要实现 GlideAppliesOptions 接口
        BaseImageLoaderStrategy baseImageLoaderStrategy = component.getImageLoader().getImageLoaderStrategy();
        if (baseImageLoaderStrategy instanceof GlideAppliesOptions) {
            ((GlideAppliesOptions) baseImageLoaderStrategy).applyGlideOptions(context, builder);
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // Glide 默认使用 HttpURLConnection 做网络请求，在这切换成 Okhttp 请求
        AppComponent appComponent = AppUtils.Companion.obtainAppComponentFromContext(context);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(appComponent.getOkHttpClient()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}