package com.tool.common.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.tool.common.frame.BasePresenter;
import com.tool.common.widget.ToastBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    /**
     * Presenter
     */
    protected P presenter = null;

    // 解除绑定
    private Unbinder unbinder = null;

    // BroadcastReceiver
    private BroadcastReceiver broadcastReceiver;

    public static final String RECEIVER_ACTION = "com.tool.common";
    public static final String RECEIVER_TYPE = "com.tool.type";
    public static final String RECEIVER_TOAST = "com.tool.toast";
    public static final String RECEIVER_MESSAGE = "com.tool.message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());

        // 绑定ButterKnife
        unbinder = ButterKnife.bind(this);

        this.componentInject();

        this.create(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 解除绑定
        unbinder.unbind();

        // 释放资源
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 获取布局文件，需要在子类中重写此方法
     */
    public abstract int getLayoutId();

    /**
     * 相当于Activity onCreate方法，需要在子类中重写此方法
     */
    public abstract void create(Bundle savedInstanceState);

    /**
     * 依赖注入
     */
    protected abstract void componentInject();

    /**
     * ActivityReceriver
     */
    class ActivityReceriver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getStringExtra(RECEIVER_TYPE)) {
                    case RECEIVER_TOAST:// 显示提示信息框
                        String message = intent.getStringExtra(RECEIVER_MESSAGE);
                        ToastBar.show(BaseActivity.this, message);
                        break;
                }
            }
        }
    }

    /**
     * 注册广播
     */
    public void registerReceiver() {
        try {
            broadcastReceiver = new ActivityReceriver();
            IntentFilter filter = new IntentFilter(RECEIVER_ACTION);
            registerReceiver(broadcastReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销广播
     */
    public void unregisterReceiver() {
        if (broadcastReceiver == null) {
            return;
        }
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }
}