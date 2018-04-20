package me.mvp.demo.app.api.service;

import me.mvp.demo.entity.User;
import me.mvp.frame.http.ResponseEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * 用户相关API
 */
public interface ApiUser {

    @FormUrlEncoded
    @POST("index.php?m=member&c=api&a=do_login")
    Call<ResponseEntity<User>> login(@Field("username") String username, @Field("password") String password);

    @Streaming
    @POST("index.php?m=member&c=api&a=do_login")
    Call<ResponseEntity<User>> test();
}