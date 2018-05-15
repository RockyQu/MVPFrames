package me.mvp.frame.http;

import me.mvp.frame.di.component.AppComponent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ResponseCallback<T> implements Callback<T> {

    private AppComponent component;

    public ResponseCallback() {
        ;
    }

    public ResponseCallback(AppComponent component) {
        this.component = component;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponse(response.body());
        }

        onFinish(true);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (component != null) {
            component.getGlobalErrorHandler().httpError(component.getApplication(), throwable);
        }

        if (!call.isCanceled()) {
            onFailure(throwable);
        }

        onFinish(!call.isCanceled());
    }

    protected abstract void onResponse(T body);

    protected abstract void onFailure(Throwable throwable);

    protected abstract void onFinish(boolean isCanceled);
}