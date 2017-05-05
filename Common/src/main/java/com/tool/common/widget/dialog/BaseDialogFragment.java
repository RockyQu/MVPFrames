package com.tool.common.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tool.common.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 使用BaseDialogFragment自定义自己需要的对话框
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener {

    // View
    private View view = null;

    // 解除绑定
    private Unbinder unbinder = null;

    /**
     * 模糊引擎
     */
    private BlurEngine dialogEngine;

    // 开启背景模糊效果,默认关闭
    public static final boolean DEFAULT_OPEN_BLUR = false;
    // 背景变暗,默认不变暗
    public static final boolean DEFAULT_BACKGROUND_DARKER = false;
    // 进入退出动画效果,默认淡入淡出效果
    public static final int DEFAULT_DIALOG_ANIMATIONS = R.style.DialogFragment_Default_Animation;
    // 背景色,默认透明色
    public static final int DEFAULT_BACKGROUND_COLOR = android.R.color.white;
    // 开启键盘自动弹出,默认关闭
    public static final boolean DEFAULT_OPEN_KEYBOARD = false;
    // 开启点击外部不消失,默认关闭
    public static final boolean DEFAULT_TOUCH_OUTSIDE = false;
    // 开启响应返回键,默认开启
    public static final boolean DEFAULT_CANCELABLE_BACK = true;
    // 禁止拦截窗口外部事件,默认关闭
    public static final boolean DEFAULT_NOT_TOUCH_MODAL = false;
    // 窗体相对位置,默认居中
    public static final int DEFAULT_RELATIVE_POSITION = Gravity.CENTER;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        if (openBlur()) {// 是否开启模糊效果
            dialogEngine = new BlurEngine(getActivity());
            dialogEngine.setBlurDegree(getBlurDegree());
            dialogEngine.setBlurRadius(getBlurRadius());
            dialogEngine.setSwitchEngine(isSwitchEngine());
            dialogEngine.setDebugMode(isDebugMode());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(getLayoutId(), null);

        // 绑定ButterKnife
        unbinder = ButterKnife.bind(this, view);

        // 创建AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setView(view);

        // 拆分系统onCreateDialog方法，提供一个create方法，基本初始化代码放到onCreateDialog执行，对于子类的初始化放到create方法执行
        this.create(savedInstanceState, view);

        return dialog.create();
    }

    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();

            // 启动时,背景是否变暗
            if (!isBackgroundDarker()) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }

            // 动画效果
            window.getAttributes().windowAnimations = dialogAnimations() != 0 ? dialogAnimations() : DEFAULT_DIALOG_ANIMATIONS;
            // 背景色
            window.setBackgroundDrawableResource(backgroundColor() != 0 ? backgroundColor() : DEFAULT_BACKGROUND_COLOR);

            // 开启键盘自动弹出
            if (isOpenKeyboard()) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }

            // 开启点击外部不消失
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside());

            // 开启响应返回键
            dialog.setCancelable(isCancelableBack());

            // 禁止拦截窗口外部事件
            if (isNotTouchModal()) {
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            }

            // 窗体相对位置
            window.setGravity(relativePosition());
        }
        super.onStart();

        dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();

            // 控制Dialog宽高,该部分功能必须写在super.onStart()之后
            // 此设置会导致键盘不自动弹出,原因不明
//            window.setLayout(window.getAttributes().width, window.getAttributes().height);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (dialogEngine != null) {
            dialogEngine.onAttach(activity);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialogEngine != null) {
            dialogEngine.onResume(getRetainInstance());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dialogEngine != null) {
            dialogEngine.onDismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (dialogEngine != null) {
            dialogEngine.onDetach();
        }
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    /**
     * 模糊程度,该值必须大于1.0
     */
    protected float getBlurDegree() {
        return BlurEngine.DEFAULT_BLUR_DEGREE;
    }

    /**
     * 模糊半径
     */
    protected int getBlurRadius() {
        return BlurEngine.DEFAULT_BLUR_RADIUS;
    }

    /**
     * 内置两种模糊引擎，用来切换，默认使用BlurFast
     */
    protected boolean isSwitchEngine() {
        return BlurEngine.DEFAULT_SWITCH_ENGINE;
    }

    /**
     * 是否启用调试模式,默认关闭（会在模糊View上显示出模糊处理所用时间）
     */
    protected boolean isDebugMode() {
        return BlurEngine.DEFAULT_DEBUG_MODE;
    }

    /**
     * 开户背景模糊效果
     * 在子类中可以重写此方法来控制此项功能
     */
    protected boolean openBlur() {
        return BaseDialogFragment.DEFAULT_OPEN_BLUR;
    }

    /**
     * 背景变暗,默认不变暗
     * 在子类中可以重写此方法来控制此项功能
     */
    protected boolean isBackgroundDarker() {
        return BaseDialogFragment.DEFAULT_BACKGROUND_DARKER;
    }

    /**
     * 对话框动画,默认淡入淡出动画效果
     * 在子类中可以重写此方法来控制此项功能
     */
    protected int dialogAnimations() {
        return BaseDialogFragment.DEFAULT_DIALOG_ANIMATIONS;
    }

    /**
     * 背景色,默认透明色
     * 在子类中可以重写此方法来控制此项功能
     */
    protected int backgroundColor() {
        return BaseDialogFragment.DEFAULT_BACKGROUND_COLOR;
    }

    /**
     * 开启键盘自动弹出,默认关闭
     * 在子类中可以重写此方法来控制此项功能
     */
    protected boolean isOpenKeyboard() {
        return BaseDialogFragment.DEFAULT_OPEN_KEYBOARD;
    }

    /**
     * 开启点击外部不消失,默认关闭
     * 在子类中可以重写此方法来控制此项功能
     */
    protected boolean isCanceledOnTouchOutside() {
        return BaseDialogFragment.DEFAULT_TOUCH_OUTSIDE;
    }

    /**
     * 开启响应返回键,默认开启
     * 在子类中可以重写此方法来控制此项功能
     */
    protected boolean isCancelableBack() {
        return BaseDialogFragment.DEFAULT_CANCELABLE_BACK;
    }

    /**
     * 禁止拦截窗口外部事件,默认关闭
     * 在子类中可以重写此方法来控制此项功能
     */
    protected boolean isNotTouchModal() {
        return BaseDialogFragment.DEFAULT_NOT_TOUCH_MODAL;
    }

    /**
     * 窗体相对位置,默认居中
     * 在子类中可以重写此方法来控制此项功能
     */
    protected int relativePosition() {
        return BaseDialogFragment.DEFAULT_RELATIVE_POSITION;
    }

    /**
     * 获取布局文件，需要在子类中重写此方法
     */
    public abstract int getLayoutId();

    /**
     * 拆分系统onCreateDialog方法，提供一个create方法，基本初始化代码放到onCreateDialog执行，对于子类的初始化放到create方法执行
     */
    public abstract void create(Bundle savedInstanceState, View view);

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        } else {
            return false;
        }
    }
}