package com.tool.common.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tool.common.R;
import com.tool.common.base.BaseActivity;
import com.tool.common.base.BaseApplication;
import com.tool.common.integration.AppManager;
import com.tool.common.utils.AnimationUtils;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;

import static com.tool.common.integration.AppManager.APPMANAGER_MESSAGE;
import static com.tool.common.integration.AppManager.START_ACTIVITY;

/**
 * ToastBar
 */
public class ToastBar {

    // Snackbar
    private Snackbar snackbar;

    public ToastBar(Snackbar snackbar) {
        this.snackbar = snackbar;

        // 默认居中
        this.setMessageGravity(Gravity.CENTER);
        // 圆角
        this.margins(30, 30, 30, 30);
        // 圆角
        this.radius(12);
        // 提示文字大小
        this.setMessageTextSize(16);

        AlphaAnimation alphaAnimation = AnimationUtils.getShowAlphaAnimation();
        getView().setAnimation(alphaAnimation);
    }

    private View getView(){
        return snackbar.getView();
    }

    /**
     * Create Snackbar
     *
     * @param view
     * @param message
     * @param duration
     * @return ToastBar
     */
    public static ToastBar create(View view, String message, int duration) {
        return new ToastBar(Snackbar.make(view, message, duration));
    }

    /**
     * Create Snackbar
     *
     * @param view
     * @param message
     * @return ToastBar
     */
    public static ToastBar create(View view, String message) {
        return create(view, message, Snackbar.LENGTH_SHORT);
    }

    private static Toast TOAST = null;

    public static void message(Context context, String message) {
        if (TOAST == null) {
            TOAST = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        else {
            TOAST.setText(message);
            TOAST.setDuration(Toast.LENGTH_SHORT);
        }
        TOAST.show();
    }

    /**
     * Create Snackbar
     *
     * @param activity
     * @param message
     * @return ToastBar
     */
    public static ToastBar create(Activity activity, String message) {
        return create(activity.getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT);
    }

    /**
     * 设置背景色
     *
     * @return ToastBar
     */
    public ToastBar setBackgroundColor(int color) {
        snackbar.getView().setBackgroundColor(color);
        return this;
    }

    /**
     * 设置提示文字内容
     *
     * @param textSize
     * @return ToastBar
     */
    public ToastBar setMessageTextSize(float textSize) {
        return setMessage(0, textSize);
    }

    /**
     * 设置文字颜色
     *
     * @param color
     * @return ToastBar
     */
    public ToastBar setMessageColor(@ColorInt int color) {
        return setMessage(color, 0);
    }

    /**
     * 设置提示文字内容、颜色
     *
     * @param color
     * @return ToastBar
     */
    public ToastBar setMessage(@ColorInt int color, float textSize) {
        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        if (color != 0) {
            textView.setTextColor(color);
        }
        if (textSize != 0) {
            textView.setTextSize(textSize);
        }
        return this;
    }

    /**
     * 设置Button文字内容
     *
     * @param message
     * @return ToastBar
     */
    public ToastBar setActionMessage(String message) {
        return setAction(message, 0, null);
    }

    /**
     * 设置Button文字内容、颜色
     *
     * @param message
     * @param color
     * @return ToastBar
     */
    public ToastBar setAction(String message, @ColorInt int color) {
        return setAction(message, color, null);
    }

    /**
     * 设置Button文字内容、颜色、点击事件
     *
     * @param message
     * @param color
     * @param onClickListener
     * @return ToastBar
     */
    public ToastBar setAction(String message, @ColorInt int color, View.OnClickListener onClickListener) {
        Button button = (Button) snackbar.getView().findViewById(R.id.snackbar_action);
        if (!TextUtils.isEmpty(message)) {
            button.setText(message);
        }
        if (color != 0) {
            button.setTextColor(color);
        }
        if (onClickListener != null) {
            button.setOnClickListener(onClickListener);
        }
        return this;
    }

    /**
     * 设置背景透明度
     *
     * @param alpha
     * @return ToastBar
     */
    public ToastBar alpha(float alpha) {
        alpha = alpha >= 1.0f ? 1.0f : (alpha <= 0.0f ? 0.0f : alpha);
        snackbar.getView().setAlpha(alpha);
        return this;
    }

    /**
     * 设置显示位置
     *
     * @param gravity
     */
    public ToastBar setLayoutGravity(int gravity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(snackbar.getView().getLayoutParams().width, snackbar.getView().getLayoutParams().height);
        params.gravity = gravity;
        snackbar.getView().setLayoutParams(params);
        return this;
    }

    /**
     * 设置文字对齐方式（居中，靠左、靠右）
     *
     * @param gravity
     * @return ToastBar
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private ToastBar setMessageGravity(int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            TextView message = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
            // View.setTextAlignment需要SDK>=17
            message.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            message.setGravity(Gravity.CENTER_VERTICAL | gravity);
        }
        return this;
    }

    /**
     * 设置TextView上下左右的图片
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return ToastBar
     */
    public ToastBar setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        TextView message = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        LinearLayout.LayoutParams paramsMessage = (LinearLayout.LayoutParams) message.getLayoutParams();
        paramsMessage = new LinearLayout.LayoutParams(paramsMessage.width, paramsMessage.height, 0.0f);
        message.setLayoutParams(paramsMessage);
        message.setCompoundDrawablePadding(message.getPaddingLeft());
        int textSize = (int) message.getTextSize();
        if (left != null) {
            left.setBounds(0, 0, textSize, textSize);
        }
        if (right != null) {
            right.setBounds(0, 0, textSize, textSize);
        }
        message.setCompoundDrawables(left, top, right, bottom);
        LinearLayout.LayoutParams paramsSpace = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        ((Snackbar.SnackbarLayout) snackbar.getView()).addView(new Space(snackbar.getView().getContext()), 1, paramsSpace);
        return this;
    }

    /**
     * 设置TextView上下左右的图片
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return ToastBar
     */
    public ToastBar setCompoundDrawables(@DrawableRes Integer left, @DrawableRes Integer top, @DrawableRes Integer right, @DrawableRes Integer bottom) {
        Drawable drawableLeft = null;
        Drawable drawableTop = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        try {
            if (left != null) {
                drawableLeft = snackbar.getView().getResources().getDrawable(left.intValue());
            }
            if (top != null) {
                drawableTop = snackbar.getView().getResources().getDrawable(top.intValue());
            }
            if (right != null) {
                drawableRight = snackbar.getView().getResources().getDrawable(right.intValue());
            }
            if (bottom != null) {
                drawableBottom = snackbar.getView().getResources().getDrawable(bottom.intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    /**
     * 设置Snackbar外边距
     *
     * @param margin
     * @return ToastBar
     */
    public ToastBar margins(int margin) {
        if (snackbar != null) {
            return margins(margin, margin, margin, margin);
        } else {
            return this;
        }
    }

    /**
     * 设置Snackbar外边距
     * 调用margins后再调用setLayoutGravity，会导致margins无效
     * 应该先调用setLayoutGravity,在show()之前调用margins
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return ToastBar
     */
    public ToastBar margins(int left, int top, int right, int bottom) {
        if (snackbar != null) {
            ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
            ((ViewGroup.MarginLayoutParams) params).setMargins(left, top, right, bottom);
            snackbar.getView().setLayoutParams(params);
        }
        return this;
    }

    /**
     * 通过SnackBar现在的背景,获取其设置圆角值时候所需的GradientDrawable实例
     *
     * @param backgroundOri
     * @return
     */
    private GradientDrawable getRadiusDrawable(Drawable backgroundOri) {
        GradientDrawable background = null;
        if (backgroundOri instanceof GradientDrawable) {
            background = (GradientDrawable) backgroundOri;
        } else if (backgroundOri instanceof ColorDrawable) {
            int backgroundColor = ((ColorDrawable) backgroundOri).getColor();
            background = new GradientDrawable();
            background.setColor(backgroundColor);
        } else {
        }
        return background;
    }

    /**
     * 设置Snackbar布局的圆角半径值
     *
     * @param radius 圆角半径
     * @return
     */
    public ToastBar radius(float radius) {
        if (snackbar != null) {
            //将要设置给mSnackbar的背景
            GradientDrawable background = getRadiusDrawable(snackbar.getView().getBackground());
            if (background != null) {
                radius = radius <= 0 ? 12 : radius;
                background.setCornerRadius(radius);
                snackbar.getView().setBackgroundDrawable(background);
            }
        }
        return this;
    }

    /**
     * 设置Snackbar布局的圆角半径值及边框颜色及边框宽度
     *
     * @param radius
     * @param strokeWidth
     * @param strokeColor
     * @return
     */
    public ToastBar radius(int radius, int strokeWidth, @ColorInt int strokeColor) {
        if (snackbar != null) {
            //将要设置给mSnackbar的背景
            GradientDrawable background = getRadiusDrawable(snackbar.getView().getBackground());
            if (background != null) {
                radius = radius <= 0 ? 12 : radius;
                strokeWidth = strokeWidth <= 0 ? 1 : (strokeWidth >= snackbar.getView().findViewById(R.id.snackbar_text).getPaddingTop() ? 2 : strokeWidth);
                background.setCornerRadius(radius);
                background.setStroke(strokeWidth, strokeColor);
                snackbar.getView().setBackgroundDrawable(background);
            }
        }
        return this;
    }

    /**
     * 显示Snackbar，最后调用show()
     */
    public void show() {
        snackbar.show();
    }

    /**
     * 通过EventBus远程遥控显示提示信息框
     *
     * @param message
     */
    public static void message(String message) {
        Message msg = new Message();
        msg.what = AppManager.SNACKBAR;
        msg.obj = message;
        EventBus.getDefault().post(msg, APPMANAGER_MESSAGE);
    }
}