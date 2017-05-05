package com.tool.common.widget.imageloader.glide;

import android.widget.ImageView;

import com.tool.common.widget.imageloader.ImageConfig;

/**
 * 图片加载的配置信息
 */
public class GlideImageConfig extends ImageConfig {

    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
    }

    public static Buidler builder() {
        return new Buidler();
    }

    public static final class Buidler {

        private String url;
        private ImageView imageView;

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

        public GlideImageConfig build() {
            if (url == null) {
                throw new IllegalStateException("Url is required");
            }
            if (imageView == null) {
                throw new IllegalStateException("ImageView is required");
            }
            return new GlideImageConfig(this);
        }
    }
}
