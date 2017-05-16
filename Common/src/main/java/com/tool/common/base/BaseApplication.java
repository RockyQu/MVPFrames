package com.tool.common.base;

import android.app.Application;

import com.tool.common.base.delegate.AppDelegate;
import com.tool.common.di.component.AppComponent;

/**
 * GitHub:https://github.com/DesignQu/MVPFrames
 *
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
 * 2、使用Activity、Fragment、ViewHolder、Service、Adapter请继承BaseActivity、BaseFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构
 * 3、通过Application获取AppComponent里面的对象可直接使用
 * 4、简单功能及页面无需引入MVP
 */
public abstract class BaseApplication extends Application implements App {

    // AppDelegate
    private AppDelegate delegate;

    @Override
    public void onCreate() {
        super.onCreate();

        this.delegate = new AppDelegate(this);
        this.delegate.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        this.delegate.onTerminate();
    }

    /**
     * 返回AppComponent提供统一出口，AppComponent里拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return delegate.getAppComponent();
    }
}