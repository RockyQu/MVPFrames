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
 * 添加水印
 */
public class WatermarkTransformation extends BitmapTransformation {

    private static Paint paint;
    String message;

    public WatermarkTransformation(Context context, String message) {
        super(context);

        this.message = message;
    }

    static {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(42);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return watermark(toTransform);
    }

    @Override
    public String getId() {
        return WatermarkTransformation.class.getSimpleName();
    }

    private Bitmap watermark(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawText(message, 20, 40, paint);
        return bitmap;
    }
}
