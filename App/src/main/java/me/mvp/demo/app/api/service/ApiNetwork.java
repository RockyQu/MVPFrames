package me.mvp.demo.app.api.service;

import me.mvp.demo.entity.User;
import me.mvp.demo.entity.base.ResponseEntity;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiNetwork {

    @FormUrlEncoded
    @POST("index.php?m=member&c=api&a=do_login")
    Call<ResponseEntity<User>> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("index.php?m=member&c=api&a=do_login")
    Observable<ResponseEntity<User>> rxLogin(@Field("username") String username, @Field("password") String password);
}