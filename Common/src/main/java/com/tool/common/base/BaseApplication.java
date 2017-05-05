package com.tool.common.base;

import android.app.Application;
import android.content.Context;

import com.tool.common.base.delegate.AppDelegateManager;
import com.tool.common.di.component.BaseComponent;
import com.tool.common.log.log.LogConfig;


/**
 * 一、项目涉及的主要框架
 * 1、Dagger2 https://google.github.io/dagger
 * 2、ButterKnife http://jakewharton.github.io/butterknife
 * 3、Retrofit2 https://github.com/square/retrofit
 * 4、Glide https://github.com/bumptech/glide
 * 5、Okhttp https://github.com/square/okhttp
 * 6、Gson https://github.com/google/gson
 * 7、GreenDAO https://github.com/greenrobot/greenDAO
 * 8、EasyPermissions https://github.com/googlesamples/easypermissions
 * 9、Q-Log https://github.com/DesignQu/Tool-Log
 * <p>
 * 二、基本使用方法
 * 1、配置自定义Application必须继承BaseApplication，BaseApplication完成Http框架、图片框架、日志管理框架等初始化工作
 * 2、使用Activity、Fragment、ViewHolder、Service、Adapter请继承CommonActivity、CommonFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构
 * 3、通过Application获取AppComponent里面的对象可直接使用
 * 4、简单功能及页面无需引入MVP
 * <p>
 * 三、未来可能会更新的一些功能
 * 1、路由框架
 * 2、用户行为分析日志模块
 * 3、优化MVP缺点（类、接口过多的问题）
 * 4、重构蓝牙定位模块
 * 5、混淆
 */
public abstract class BaseApplication extends Application implements App {

    // Context
    private static Context context;

    // Log配置
    private LogConfig logConfig;

    // AppDelegateManager
    private AppDelegateManager delegate;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = this;

        // Log配置
        this.logConfig = LogConfig.Buidler
                .buidler()
                .setContext(this)
                .setOpen(logSwitch())
                .build();

        this.delegate = new AppDelegateManager(this);
        this.delegate.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (context != null) {
            this.context = this;
        }
        if (logConfig != null) {
            this.logConfig = null;
        }

        this.delegate.onTerminate();
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 日志开关
     *
     * @return
     */
    protected abstract boolean logSwitch();

    /**
     * 返回AppComponent提供统一出口，AppComponent里拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public BaseComponent getBaseComponent() {
        return delegate.getBaseComponent();
    }
}