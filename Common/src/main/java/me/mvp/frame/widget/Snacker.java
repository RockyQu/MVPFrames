package me.mvp.frame.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.mvp.frame.R;
import me.mvp.frame.integration.AppManager;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * 提示框
 */
public class Snacker implements View.OnClickListener {

    private static final int DEFAULT_VALUE = -100000;

    private WeakReference<LinearLayout> layoutWeakReference;
    private WeakReference<Activity> activityWeakReference;

    // 显示时长
    private int duration = 3000;

    // 背景颜色
    private int backgroundColor = DEFAULT_VALUE;

    // 提示框高度
    private int height = DEFAULT_VALUE;

    // 左侧图标
    private int icon = DEFAULT_VALUE;
    // 左侧图标
    private Drawable iconDrawable = null;
    // 图标颜色
    private int iconColorFilterColor = DEFAULT_VALUE;
    // 图标尺寸
    private int iconSize = 24;

    // 提示文字信息
    private String message = "";
    // 提示文字信息颜色
    private int messageColor = DEFAULT_VALUE;

    // 点击事件
    private OnToastBarClickListener onToastBarClickListener = null;

    private Snacker(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);

        setDefault();
    }

    /**
     * Create Snacker with activity reference
     *
     * @param activity
     * @return return Snacker
     */
    public static Snacker with(Activity activity) {
        return new Snacker(activity);
    }

    /**
     * Return activity parent view
     *
     * @return return Snacker
     */
    private ViewGroup getActivityDecorView() {
        return (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
    }

    private void setDefault() {
        this.duration = 3000;
        this.backgroundColor = DEFAULT_VALUE;

        this.height = DEFAULT_VALUE;

        this.icon = DEFAULT_VALUE;
        this.iconDrawable = null;
        this.iconColorFilterColor = DEFAULT_VALUE;
        this.iconSize = 24;

        this.message = "";
        this.messageColor = DEFAULT_VALUE;

        this.onToastBarClickListener = null;
    }

    /**
     * Return Context
     *
     * @return return Snacker
     */
    private Context getContext() {
        return activityWeakReference.get();
    }

    /**
     * Return View
     *
     * @return return Snacker
     */
    private View getLayout() {
        return layoutWeakReference.get();
    }

    /**
     * 设置显示时长
     *
     * @param duration
     * @return return Snacker
     */
    public Snacker setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor
     * @return return Snacker
     */
    public Snacker setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * 设置提示框高度
     *
     * @param height
     * @return return Snacker
     */
    public Snacker setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * 设置左侧图标
     *
     * @param icon
     * @return return Snacker
     */
    public Snacker setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    /**
     * 设置左侧图标
     *
     * @param iconDrawable
     * @return return Snacker
     */
    public Snacker setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        return this;
    }

    /**
     * 设置图标颜色
     *
     * @param iconColorFilterColor
     * @return return Snacker
     */
    public Snacker setIconColorFilterColor(int iconColorFilterColor) {
        this.iconColorFilterColor = iconColorFilterColor;
        return this;
    }

    /**
     * 设置图标尺寸
     *
     * @param iconSize
     * @return return Snacker
     */
    public Snacker setIconSize(int iconSize) {
        this.iconSize = iconSize;
        return this;
    }

    /**
     * 设置提示框文字颜色
     *
     * @param messageColor
     * @return return Snacker
     */
    public Snacker setMessageColor(int messageColor) {
        this.messageColor = messageColor;
        return this;
    }

    /**
     * 设置ToastBar点击事件
     *
     * @param onToastBarClickListener
     * @return return Snacker
     */
    public Snacker setOnToastBarClickListener(OnToastBarClickListener onToastBarClickListener) {
        this.onToastBarClickListener = onToastBarClickListener;
        return this;
    }

    /**
     * 成功
     *
     * @return return Snacker
     */
    public Snacker success() {
        this.backgroundColor = Color.parseColor("#2bb600");
        this.messageColor = Color.parseColor("#FFFFFF");

        this.icon = R.drawable.ic_success;
        this.iconColorFilterColor = Color.parseColor("#FFFFFF");

        return this;
    }

    /**
     * 警告
     *
     * @return return Snacker
     */
    public Snacker warning() {
        this.backgroundColor = Color.parseColor("#ffc100");
        this.messageColor = Color.parseColor("#000000");

        this.icon = R.drawable.ic_warning;
        this.iconColorFilterColor = Color.parseColor("#000000");

        return this;
    }

    /**
     * 错误
     *
     * @return return Snacker
     */
    public Snacker error() {
        this.backgroundColor = Color.parseColor("#ff0000");
        this.messageColor = Color.parseColor("#FFFFFF");

        this.icon = R.drawable.ic_error;
        this.iconColorFilterColor = Color.parseColor("#FFFFFF");

        return this;
    }

    /**
     * 设置提示信息文字，默认为 {@link Snacker#success()} 类型
     *
     * @param message
     * @return return Snacker
     * @see #success() 成功
     * @see #warning() 警告
     * @see #error() 错误
     */
    public Snacker setMessage(String message) {
        this.message = message;
        return success();
    }

    /**
     * 显示提示框，请在最后调用
     *
     * @return return Snacker
     */
    public Snacker show() {
        if (getContext() != null) {
            createView();
        }

        return null;
    }

    /**
     * 移除
     */
    private void hide() {
        if (getLayout() != null) {
            getLayout().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.snacker_hide));
            getActivityDecorView().removeView(getLayout());
        }
    }

    /**
     * 创建并添加提示框
     */
    private void createView() {
        // Main layout
        LinearLayout layout = new LinearLayout(getContext());
        layoutWeakReference = new WeakReference<>(layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.height == DEFAULT_VALUE ? (getStatusBarHeight() + convertToDp(56)) : convertToDp(this.height));
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(46, getStatusBarHeight(), 46, 0);

        // 控件高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setElevation(6);
        }

        // Background color
        layout.setBackgroundColor(this.backgroundColor);

        // Icon
        // If icon is set
        if (this.icon != DEFAULT_VALUE || this.iconDrawable != null) {
            AppCompatImageView ivIcon = new AppCompatImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(convertToDp(this.iconSize), convertToDp(this.iconSize));
            ivIcon.setLayoutParams(lp);

            if (this.icon == DEFAULT_VALUE) {
                ivIcon.setImageDrawable(this.iconDrawable);
            } else {
                ivIcon.setImageResource(this.icon);
            }
            ivIcon.setClickable(false);
            if (this.iconColorFilterColor != DEFAULT_VALUE) {
                ivIcon.setColorFilter(this.iconColorFilterColor);
            }
            layout.addView(ivIcon);
        }

        // Title and description
        LinearLayout textLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayout.setLayoutParams(textLayoutParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (!this.message.isEmpty()) {
            TextView tvTitle = new TextView(getContext());
            tvTitle.setLayoutParams(lpTv);
            tvTitle.setGravity(Gravity.CENTER_VERTICAL);

            tvTitle.setPadding(46, 0, 26, 0); // No top padding if there is no message
            if (messageColor != DEFAULT_VALUE) {
                tvTitle.setTextColor(messageColor);
            }

            tvTitle.setTextSize(14);
            tvTitle.setText(this.message);
            tvTitle.setClickable(false);
            textLayout.addView(tvTitle);
        }

        layout.addView(textLayout);
        layout.setId(R.id.Snacker);

        ViewGroup viewGroup = getActivityDecorView();
        getExistingOverlayInViewAndRemove(viewGroup);

        layout.setOnClickListener(this);
        viewGroup.addView(layout);

        layout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.snacker_show));

        Handler handler = new Handler();
        handler.removeCallbacks(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, this.duration);
    }

    /**
     * Gets the existing sneaker and removes before adding new one
     *
     * @param parent
     */
    public void getExistingOverlayInViewAndRemove(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getId() == R.id.Snacker) {
                parent.removeView(child);
            }
            if (child instanceof ViewGroup) {
                getExistingOverlayInViewAndRemove((ViewGroup) child);
            }
        }
    }

    /**
     * Returns status bar height.
     *
     * @return return to the status bar height.
     */
    private int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = ((Activity) getContext()).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        return statusBarHeight;
    }

    private int convertToDp(float sizeInDp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

    /**
     * 点击并移除
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (onToastBarClickListener != null) {
            onToastBarClickListener.onClick(view);
        }
        getLayout().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.snacker_hide));
        getActivityDecorView().removeView(getLayout());
    }

    public interface OnToastBarClickListener {
        void onClick(View view);
    }

    /**
     * 通过EventBus远程遥控显示 {@link Snacker}
     *
     * @param message
     */
    public static void postMessage(String message) {
        Message msg = new Message();
        msg.what = AppManager.SNACKER;
        msg.obj = message;
        EventBus.getDefault().post(msg, AppManager.APPMANAGER_MESSAGE);
    }
}