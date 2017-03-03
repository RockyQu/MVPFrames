package com.frame.mvp.di.common.module;

import com.frame.mvp.app.api.service.ApiCommon;
import com.frame.mvp.app.api.service.ApiUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * ServiceModule
 */
@Module
public class ApiModule {

    @Singleton
    @Provides
    ApiCommon provideApiCommon(Retrofit retrofit) {
        return retrofit.create(ApiCommon.class);
    }

    @Singleton
    @Provides
    ApiUser provideApiUser(Retrofit retrofit) {
        return retrofit.create(ApiUser.class);
    }
}