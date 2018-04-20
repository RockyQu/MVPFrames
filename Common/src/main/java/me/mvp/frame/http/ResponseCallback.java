package me.mvp.frame.http;

import me.mvp.frame.http.exception.ApiException;
import me.mvp.frame.http.exception.FactoryException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        onFinish(true);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (!call.isCanceled()) {
            onFailure(FactoryException.resolveExcetpion(throwable));
        }

        onFinish(!call.isCanceled());
    }

    protected abstract void onResponse(T body);

    protected abstract void onFailure(ApiException exception);

    protected abstract void onFinish(boolean isCanceled);
}