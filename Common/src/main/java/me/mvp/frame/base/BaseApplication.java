package me.mvp.frame.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import me.logg.Logg;
import me.mvp.frame.base.delegate.ApplicationDelegate;
import me.mvp.frame.base.delegate.ApplicationLifecycles;
import me.mvp.frame.di.component.AppComponent;
import me.mvp.frame.utils.AppUtils;
import me.mvp.frame.utils.ExceptionUtils;

/**
 * @see <a href="https://github.com/RockyQu/MVPFrames"></a>
 */
public class BaseApplication extends Application implements App {

    // ApplicationDelegate
    private ApplicationLifecycles delegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (delegate == null) {
            this.delegate = new ApplicationDelegate(base);
        }

        this.delegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (delegate != null) {
            this.delegate.onCreate(this);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (delegate != null) {
            this.delegate.onTerminate(this);
        }
    }

    /**
     * 返回 {@link AppComponent} 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例都可以直接使用
     *
     * @return AppComponent
     * @see AppUtils#obtainAppComponentFromContext(Context) 可直接获取 {@link AppComponent}
     */
    @Override
    public AppComponent getAppComponent() {
        ExceptionUtils.checkNotNull(delegate, "%s cannot be null", ApplicationLifecycles.class.getName());
        ExceptionUtils.checkState(delegate instanceof App, "%s must be implements %s", ApplicationLifecycles.class.getName(), App.class.getName());
        return ((App) delegate).getAppComponent();
    }
}