package com.tool.common.utils;

import android.graphics.Bitmap;

import com.tool.common.utils.base.BaseUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * BitmapUtils
 */
public class BitmapUtils extends BaseUtils {

    public BitmapUtils() {
        super();
    }

    /**
     * Bitmap保存成图片
     *
     * @param bitmap Bitmap
     * @param path   保存路径
     * @return 图片保存路径
     */
    public static String saveBitmapToFile(Bitmap bitmap, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return path;
    }
}