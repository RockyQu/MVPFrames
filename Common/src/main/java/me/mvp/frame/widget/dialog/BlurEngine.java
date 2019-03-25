package me.mvp.frame.widget.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import me.mvp.frame.utils.BlurFastHelper;
import me.mvp.frame.utils.BlurRenderScriptHelper;

/**
 * 模糊引擎
 * 为DialogFragment提供背景模糊效果，使用时请注意BlurEngine的生命周期要与DialogFragment生命周期进行绑定
 */
public class BlurEngine {

    // 模糊程度
    public static final float DEFAULT_BLUR_DEGREE = 4.0f;
    // 模糊半径
    public static final int DEFAULT_BLUR_RADIUS = 8;
    // 内置两种模糊引擎，用来切换，默认使用BlurFast
    public static final boolean DEFAULT_SWITCH_ENGINE = false;
    // 是否启用调试模式,默认关闭（会在模糊View上显示出模糊处理所用时间）
    public static final boolean DEFAULT_DEBUG_MODE = false;

    private float blurDegree = DEFAULT_BLUR_DEGREE;
    private int blurRadius = DEFAULT_BLUR_RADIUS;
    private boolean switchEngine = DEFAULT_SWITCH_ENGINE;
    private boolean debugMode = DEFAULT_DEBUG_MODE;

    public void setBlurDegree(float blurDegree) {
        this.blurDegree = blurDegree;
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
    }

    public void setSwitchEngine(boolean switchEngine) {
        this.switchEngine = switchEngine;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    // 模糊背景的View
    private ImageView blurredBackgroundView;

    // 布局参数用于添加模糊背景
    private FrameLayout.LayoutParams blurredBackgroundLayoutParams;

    // 异步任务，用来捕获屏幕的Bitmap并执行模糊处理
    private BlurAsyncTask bluringTask;

    // Activity
    private Activity activity;

    public BlurEngine(Activity activity) {
        this.activity = activity;
    }

    /**
     * 请把该方法链接到DialogFragment原始的生命周期
     *
     * @param activity
     */
    public void onAttach(Activity activity) {
        this.activity = activity;
    }

    /**
     * 请把该方法链接到DialogFragment原始的生命周期
     *
     * @param retainedInstance use getRetainInstance.
     */
    public void onResume(boolean retainedInstance) {
        if (blurredBackgroundView == null || retainedInstance) {
            if (activity.getWindow().getDecorView().isShown()) {
                bluringTask = new BlurAsyncTask();
                bluringTask.execute();
            } else {
                activity.getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                // dialog can have been closed before being drawn
                                if (activity != null) {
                                    activity.getWindow().getDecorView()
                                            .getViewTreeObserver().removeOnPreDrawListener(this);
                                    bluringTask = new BlurAsyncTask();
                                    bluringTask.execute();
                                }
                                return true;
                            }
                        }
                );
            }
        }
    }

    /**
     * 请把该方法链接到DialogFragment原始的生命周期
     */
    @SuppressLint("NewApi")
    public void onDismiss() {
        //remove blurred background and clear memory, could be null if dismissed before blur effect
        //processing ends
        //cancel async task
        if (bluringTask != null) {
            bluringTask.cancel(true);
        }
        if (blurredBackgroundView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                blurredBackgroundView
                        .animate()
                        .alpha(0f)
                        .setDuration(300)
                        .setInterpolator(new AccelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                removeBlurredView();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                super.onAnimationCancel(animation);
                                removeBlurredView();
                            }
                        }).start();
            } else {
                removeBlurredView();
            }
        }
    }

    /**
     * 请把该方法链接到DialogFragment原始的生命周期
     */
    public void onDetach() {
        if (bluringTask != null) {
            bluringTask.cancel(true);
        }
        bluringTask = null;
        activity = null;
    }

    /**
     * 将所给Bitmap完成模糊后添加到View中
     *
     * @param bkg
     * @param view
     */
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        //define layout params to the previous imageView in order to match its parent
        blurredBackgroundLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        //overlay used to build scaled preview and blur background
        Bitmap overlay = null;

        //evaluate top offset due to status bar
        int statusBarHeight = 0;
        if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0) {
            //not in fullscreen mode
            statusBarHeight = getStatusBarHeight();
        }

        // check if status bar is translucent to remove status bar offset in order to provide blur
        // on content bellow the status.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && isStatusBarTranslucent()) {
            statusBarHeight = 0;
        }

        final int topOffset = statusBarHeight;
        // evaluate bottom or right offset due to navigation bar.
        int bottomOffset = 0;
        int rightOffset = 0;
        final int navBarSize = getNavigationBarOffset();

        bottomOffset = navBarSize;

        //add offset to the source boundaries since we don't want to blur actionBar pixels
        Rect srcRect = new Rect(
                0,
                topOffset,
                bkg.getWidth() - rightOffset,
                bkg.getHeight() - bottomOffset
        );

        //in order to keep the same ratio as the one which will be used for rendering, also
        //add the offset to the overlay.
        double height = Math.ceil((view.getHeight() - topOffset - bottomOffset) / blurDegree);
        double width = Math.ceil(((view.getWidth() - rightOffset) * height
                / (view.getHeight() - topOffset - bottomOffset)));

        // Render script doesn't work with RGB_565
        if (switchEngine) {
            overlay = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        } else {
            overlay = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.RGB_565);
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                    || activity instanceof AppCompatActivity) {
                //add offset as top margin since actionBar height must also considered when we display
                // the blurred background. Don't want to draw on the actionBar.
                blurredBackgroundLayoutParams.setMargins(0, 0, 0, 0);
                blurredBackgroundLayoutParams.gravity = Gravity.TOP;
            }
        } catch (NoClassDefFoundError e) {
            // no dependency to appcompat, that means no additional top offset due to actionBar.
            blurredBackgroundLayoutParams.setMargins(0, 0, 0, 0);
        }
        //scale and draw background view on the canvas overlay
        Canvas canvas = new Canvas(overlay);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);

        //build drawing destination boundaries
        final RectF destRect = new RectF(0, 0, overlay.getWidth(), overlay.getHeight());

        //draw background from source area in source background to the destination area on the overlay
        canvas.drawBitmap(bkg, srcRect, destRect, paint);

        //apply fast blur on overlay
        if (switchEngine) {
            overlay = BlurRenderScriptHelper.doBlur(overlay, blurRadius, true, activity);
        } else {
            overlay = BlurFastHelper.doBlur(overlay, blurRadius, true);
        }
        if (debugMode) {
            String blurTime = (System.currentTimeMillis() - startMs) + " ms";
            Rect bounds = new Rect();
            Canvas canvas1 = new Canvas(overlay);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setTextSize(20.0f);
            paint.getTextBounds(blurTime, 0, blurTime.length(), bounds);
            canvas1.drawText(blurTime, 2, bounds.height(), paint);
        }
        //set bitmap in an image view for final rendering
        blurredBackgroundView = new ImageView(activity);
        blurredBackgroundView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        blurredBackgroundView.setImageDrawable(new BitmapDrawable(activity.getResources(), overlay));
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = activity.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @return
     */
    private int getNavigationBarOffset() {
        int result = 0;
        Resources resources = activity.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 状态栏是否为半透明
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isStatusBarTranslucent() {
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{android.R.attr.windowTranslucentStatus};
        TypedArray array = activity.obtainStyledAttributes(typedValue.resourceId, attribute);
        boolean isStatusBarTranslucent = array.getBoolean(0, false);
        array.recycle();
        return isStatusBarTranslucent;
    }

    /**
     * 从根视图中移除模糊视图
     */
    private void removeBlurredView() {
        if (blurredBackgroundView != null) {
            ViewGroup parent = (ViewGroup) blurredBackgroundView.getParent();
            if (parent != null) {
                parent.removeView(blurredBackgroundView);
            }
            blurredBackgroundView = null;
        }
    }

    /**
     * 这是一个异步任务，用来捕获屏幕的Bitmap
     */
    private class BlurAsyncTask extends AsyncTask<Void, Void, Void> {

        private Bitmap bitmap;
        private View view;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            view = activity.getWindow().getDecorView();

            //retrieve background view, must be achieved on ui thread since
            //only the original thread that created a view hierarchy can touch its views.

            Rect rect = new Rect();
            view.getWindowVisibleDisplayFrame(rect);
            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            bitmap = view.getDrawingCache(true);

            /**
             * After rotation, the DecorView has no height and no width. Therefore
             * .getDrawingCache() return null. That's why we  have to force measure and layout.
             */
            if (bitmap == null) {
                view.measure(
                        View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY)
                );
                view.layout(0, 0, view.getMeasuredWidth(),
                        view.getMeasuredHeight());
                view.destroyDrawingCache();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache(true);
                bitmap = view.getDrawingCache(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            //process to the blue
            if (!isCancelled()) {
                blur(bitmap, view);
            } else {
                return null;
            }
            //clear memory
            bitmap.recycle();
            return null;
        }

        @Override
        @SuppressLint("NewApi")
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(false);

            activity.getWindow().addContentView(
                    blurredBackgroundView,
                    blurredBackgroundLayoutParams
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                blurredBackgroundView.setAlpha(0f);
                blurredBackgroundView
                        .animate()
                        .alpha(1f)
                        .setDuration(300)
                        .setInterpolator(new LinearInterpolator())
                        .start();
            }
            view = null;
            bitmap = null;
        }
    }
}