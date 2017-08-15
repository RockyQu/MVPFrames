package com.tool.common.http;

import com.tool.common.http.exception.ApiException;

public interface HttpCallback<T> {

    /**
     * Invoked for a received HTTP response.
     */
    void onResponse(T body);

    /**
     * On Failure
     */
    void onFailure(ApiException exception);

    /**
     * On Finish
     */
    void onFinish();
}