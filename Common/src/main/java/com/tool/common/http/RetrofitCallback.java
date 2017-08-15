package com.tool.common.http;

import android.app.Application;
import android.content.Context;

import com.tool.common.R;
import com.tool.common.log.QLog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Response Callback
 */
public abstract class RetrofitCallback<T> implements Callback<T> {

    private Application application;

    public RetrofitCallback(Application application) {
        this.application = application;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        // Returns the HTTP status code.
        int statusCode = response.raw().code();

        if (statusCode == 200) {
            onResponse(response.body());
        } else {
            onFailure(formatError(application, statusCode));
        }

        onFinish();
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (!call.isCanceled()) {
            onFailure(formatError(application, throwable));
        }

        onFinish();
    }

    /**
     * Error Throwable
     *
     * @param context
     * @param code
     */
    private String formatError(Context context, int code) {
        String result;
        switch (code) {
            case 404:
                result = context.getString(R.string.errorCode404);
                break;
            case 500:
                result = context.getString(R.string.errorCode500);
                break;
            case 502:
                result = context.getString(R.string.errorCode502);
                break;
            default:
                result = context.getString(R.string.errorCodeDefault) + code;
                break;
        }
        return result;
    }

    /**
     * Error Throwable
     *
     * @param context
     * @param throwable
     */
    private String formatError(Context context, Throwable throwable) {
        String result;

        if (throwable == null || throwable.getMessage() == null) {
            return "There is no error message!";
        }

        if (throwable instanceof SocketTimeoutException) {
            result = context.getString(R.string.errorSocketTimeout);
        } else if (throwable instanceof ConnectException) {
            result = context.getString(R.string.errorConnect);
        } else {
            result = context.getString(R.string.errorDefault);
        }

        return result;
    }

    /**
     * Invoked for a received HTTP response.
     */
    protected abstract void onResponse(T body);

    /**
     * On Failure
     */
    protected abstract void onFailure(String message);

    /**
     * On Finish
     */
    protected abstract void onFinish();
}