package com.frame.mvp.app.api.service;

import com.frame.mvp.entity.User;
import com.tool.common.http.ResponseEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 用户相关API
 */
public interface ApiUser {

    @FormUrlEncoded
    @POST("index.php?m=member&c=api&a=do_login")
    Call<ResponseEntity<User>> login(@Field("username") String username, @Field("password") String password);
}