package com.frame.mvp.app;

import android.app.Application;
import android.content.Context;

import com.jess.arms.base.BaseApplication;
import com.jess.arms.http.GlobeHttpResultHandler;
import com.jess.arms.utils.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.jessyan.mvparms.demo.di.component.AppComponent;
import me.jessyan.mvparms.demo.di.component.DaggerAppComponent;
import me.jessyan.mvparms.demo.di.module.CacheModule;
import me.jessyan.mvparms.demo.di.module.ServiceModule;
import me.jessyan.mvparms.demo.mvp.model.api.Api;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErroListener;
import okhttp3.Interceptor;
import okhttp3.Response;
import timber.log.Timber;

/**
 *
 */
public class MVPApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

    }
}
