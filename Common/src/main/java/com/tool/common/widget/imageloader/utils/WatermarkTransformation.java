package com.tool.common.widget.imageloader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 添加文字水印
 */
public class WatermarkTransformation extends BitmapTransformation {

    // Paint
    private Paint paint;

    // 文字
    private String message;
    // 文字大小
    private int textSize;
    // 文字颜色
    private int color;

    public WatermarkTransformation(Context context, Buidler builder) {
        super(context);

        this.message = builder.message;
        this.textSize = builder.textSize;
        this.color = builder.color;

        paint = new Paint();
        paint.setColor(color != 0 ? color : Color.RED);
        paint.setTextSize(textSize);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return watermark(toTransform);
    }

    private Bitmap watermark(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawText(message, 40, 40, paint);
        return bitmap;
    }

    public static Buidler builder() {
        return new Buidler();
    }

    public static class Buidler {

        private Context context = null;

        private String message;
        private int textSize = 40;
        private int color;

        private Buidler() {
            ;
        }

        public Buidler context(Context context) {
            this.context = context;
            return this;
        }

        public Buidler message(String message) {
            this.message = message;
            return this;
        }

        public Buidler textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Buidler color(int color) {
            this.color = color;
            return this;
        }

        public WatermarkTransformation build() {
            if (context == null) {
                throw new IllegalStateException("Context is required");
            }
            return new WatermarkTransformation(context, this);
        }
    }

    @Override
    public String getId() {
        return WatermarkTransformation.class.getSimpleName();
    }
}