package me.mvp.frame.widget.imageloader;

import androidx.annotation.IntDef;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @desc :0 = DiskCacheStrategy.all，1 = DiskCacheStrategy.NONE，2 = DiskCacheStrategy.SOURCE，3 = DiskCacheStrategy.RESULT
 * @see {@link DiskCacheStrategy}
 */
public interface CacheStrategy {

    int ALL = 0;

    int NONE = 1;

    int RESOURCE = 2;

    int DATA = 3;

    int AUTOMATIC = 4;

    @IntDef({ALL, NONE, RESOURCE, DATA, AUTOMATIC})
    @Retention(RetentionPolicy.SOURCE)
    @interface Strategy {
    }
}