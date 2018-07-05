package me.mvp.frame.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import me.mvp.frame.http.NetworkInterceptorHandler;
import me.mvp.frame.http.converter.GsonConverterBodyFactory;
import me.mvp.frame.http.converter.JsonConverterFactory;
import me.mvp.frame.http.interceptor.NetworkInterceptor;
import me.mvp.frame.utils.FileUtils;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 数据库 参数配置
 * <p>
 * 使用 2017 Google IO 大会 Architecture Components 架构组件 Room
 *
 * @see <a href="https://developer.android.google.cn/training/data-storage/room/"></a>
 */
@Module
public class DBModule {


//    @Singleton
//    @Provides
//    <T extends RoomDatabase> RoomDatabase provideDB(Application application, @NonNull Class<T> klass, DBModule.DBConfiguration dbConfiguration, @Nullable DBModule.Builder builder) {
//        return Room.databaseBuilder(application, klass, "database-name").build();
//    }
//
//    /**
//     * 提供一个OkHttp配置接口，用于对OkHttp进行格外的参数配置
//     */
//    public interface DBConfiguration {
//
//        void configOkHttp(Context context, DBModule.Builder builder);
//
//        DBConfiguration EMPTY = new DBConfiguration() {
//
//            @Override
//            public void configOkHttp(Context context, DBModule.Builder builder) {
//
//            }
//        };
//    }
//
//    public static final class Builder {
//
//        public OkHttpClient build() {
//            return new OkHttpClient(this);
//        }
//    }
}