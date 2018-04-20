package me.mvp.frame.di.module;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.mvp.frame.integration.IRepositoryManager;
import me.mvp.frame.integration.RepositoryManager;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * AppModule
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    public Gson provideGson(Application application, GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null) {
            configuration.configGson(application, builder);
        }
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Map<String, Object> provideExtras() {
        return new ArrayMap<>();
    }

    public interface GsonConfiguration {

        void configGson(Context context, GsonBuilder builder);

        GsonConfiguration EMPTY = new GsonConfiguration() {

            @Override
            public void configGson(Context context, GsonBuilder builder) {

            }
        };
    }

    public interface ErrorConfiguration {

        void error(Context context, Throwable throwable);

        ErrorConfiguration EMPTY = new ErrorConfiguration() {

            @Override
            public void error(Context context, Throwable throwable) {

            }
        };
    }
}