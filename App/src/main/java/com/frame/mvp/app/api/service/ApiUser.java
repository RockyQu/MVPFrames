package com.frame.mvp.app.api.service;

import com.frame.mvp.entity.User;
import com.tool.common.http.ResponseEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户相关API
 */
public interface ApiUser {

    // 登录
    @FormUrlEncoded
    @POST("test")
    Call<ResponseEntity<User>> login(@Field("username") String username, @Field("password") String password);
}