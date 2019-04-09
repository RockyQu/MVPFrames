package me.mvp.frame.widget.imageloader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.Nullable;
import me.mvp.frame.utils.Preconditions;

/**
 * {@link ImageLoader} 使用策略模式和建造者模式，可以动态切换图片请求框架
 */
@Singleton
public class ImageLoader {

    @Inject
    @Nullable
    BaseImageLoaderStrategy strategy;

    @Inject
    public ImageLoader() {

    }

    /**
     * 加载图片
     * <p>
     * eg:component.getImageLoader().load(component.getApplication(), GlideConfig.builder().build());
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void load(Context context, T config) {
        Preconditions.checkNotNull(strategy, "Please implement BaseImageLoaderStrategy and call AppConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule");
        this.strategy.load(context, config);
    }

    /**
     * 停止加载或清理缓存
     * <p>
     * eg:component.getImageLoader().clear(component.getApplication(), GlideConfig.builder().build());
     *
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        Preconditions.checkNotNull(strategy, "Please implement BaseImageLoaderStrategy and call AppConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy) in the applyOptions method of ConfigModule");
        this.strategy.clear(context, config);
    }

    public void setImageLoader(BaseImageLoaderStrategy strategy) {
        Preconditions.checkNotNull(strategy, "strategy == null");
        this.strategy = strategy;
    }

    public BaseImageLoaderStrategy getImageLoaderStrategy() {
        return strategy;
    }
}