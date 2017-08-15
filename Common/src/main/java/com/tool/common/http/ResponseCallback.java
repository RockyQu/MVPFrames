package com.tool.common.http;

import com.tool.common.http.exception.ApiException;
import com.tool.common.http.exception.FactoryException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Response Callback
 */
public abstract class ResponseCallback<T> implements Callback<T> {

    public ResponseCallback() {
        ;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponse(response.body());
        } else {
            onFailure(FactoryException.resolveExcetpion(response.code()));
        }

        onFinish();
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (!call.isCanceled()) {
            onFailure(FactoryException.resolveExcetpion(throwable));
        }

        onFinish();
    }

    protected abstract void onResponse(T body);

    protected abstract void onFailure(ApiException exception);

    protected abstract void onFinish();
}