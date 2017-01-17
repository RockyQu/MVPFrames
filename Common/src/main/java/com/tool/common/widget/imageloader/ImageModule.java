package com.tool.common.widget.imageloader;

/**
 * 图片框架
 */
public class ImageModule {

    private volatile static ImageModule imageManager;

    // ImageLoader
    private ImageLoader imageLoader;

    public ImageModule(ImageModule.Buidler buidler) {
        this.imageLoader = buidler.imageLoader;
    }

    public static ImageModule getInstance(ImageModule.Buidler buidler) {
        if (imageManager == null) {
            synchronized (ImageModule.class) {
                if (imageManager == null) {
                    imageManager = new ImageModule(buidler);
                }
            }
        }
        return imageManager;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 图片模块配置
     */
    public static class Buidler {

        // ImageLoader
        private ImageLoader imageLoader;

        private Buidler() {
            ;
        }

        public static ImageModule.Buidler buidler() {
            return new ImageModule.Buidler();
        }

        public ImageModule.Buidler imageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public ImageModule build() {
            return ImageModule.getInstance(this);
        }
    }
}