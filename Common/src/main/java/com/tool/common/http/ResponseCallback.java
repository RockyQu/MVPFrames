package com.tool.common.http;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Response Callback
 */
public abstract class ResponseCallback<T extends ResponseEntity> implements Callback<T> {

    public ResponseCallback() {

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.raw().code() == 200) {// Returns the HTTP status code.
            onResponse(response.body());
        } else {
            onFailure(response);
        }

        onFinish();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {
            //
        } else if (t instanceof ConnectException) {
            //
        } else if (t instanceof RuntimeException) {
            //
        }

        onFinish();
    }

    /**
     * Invoked for a received HTTP response.
     */
    protected abstract void onResponse(T body);

    /**
     * On Failure
     */
    protected abstract void onFailure(Response<T> response);

    /**
     * On Finish
     */
    protected abstract void onFinish();
}