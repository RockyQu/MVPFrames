package me.mvp.frame.widget.imageloader.glide;

public enum ImageScaleType {

    // 把图片按比例扩大/缩小到 View 的宽度，居中显示
    FIT_CENTER,

    // 按比例扩大图片的 size 居中显示，使得图片长/宽等于或大于 View 的长/宽 
    CENTER_CROP,

    // 将图片的内容完整居中显示，通过按比例缩小或原来的 size 使得图片长/宽等于或小于 View 的长/宽  
    CENTER_INSIDE
}