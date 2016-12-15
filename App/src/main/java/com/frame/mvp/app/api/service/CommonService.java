package com.frame.mvp.app.api.service;

import com.frame.mvp.entity.User;

import retrofit2.http.POST;

/**
 *
 */
public interface CommonService {

    @POST("")
    User login();
}