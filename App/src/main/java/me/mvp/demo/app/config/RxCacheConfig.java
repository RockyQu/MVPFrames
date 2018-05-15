package me.mvp.demo.app.config;

import android.content.Context;

import io.rx_cache2.internal.RxCache;
import me.mvp.frame.di.module.HttpModule;

/**
 * 扩展自定义配置 RxCache 参数
 */
public class RxCacheConfig implements HttpModule.RxCacheConfiguration {

    @Override
    public RxCache configRxCache(Context context, RxCache.Builder builder) {
        // 想自定义 RxCache 的缓存文件夹或者解析方式，如改成 fastjson，请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
        return null;
    }
}