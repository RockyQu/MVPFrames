package com.frame.mvp.mvp.main.fragment.main.network;

import com.frame.mvp.entity.User;
import com.tool.common.di.component.AppComponent;
import com.tool.common.frame.simple.BaseSimplePresenter;
import com.tool.common.frame.simple.Message;
import com.tool.common.http.ResponseCallback;
import com.tool.common.http.ResponseEntity;
import com.tool.common.http.exception.ApiException;

import retrofit2.Call;

/**
 * Presenter
 */
public class NetworkPresenter extends BaseSimplePresenter<NetworkRepository> {

    private Call<ResponseEntity<User>> login = null;

    public static final int FLAG_LOGIN = 1;

    public NetworkPresenter(AppComponent component) {
        super(component.getRepositoryManager().createRepository(NetworkRepository.class));
    }

    public void login(final Message msg, String username, String password) {
        // 回调至实现了ISimpleView接口的showLoading方法，用来显示显示等待框
        msg.getTarget().showLoading();

        // 准备接口开始调用接口请求数据
        login = model.login(username, password);
        login.enqueue(new ResponseCallback<ResponseEntity<User>>() {

            @Override
            protected void onResponse(ResponseEntity<User> body) {
                if (!body.isSuccess()) {
                    User user = body.getData();
                    if (user != null) {
                        msg.what = NetworkPresenter.FLAG_LOGIN;
                        msg.obj = user;
                        msg.HandleMessageToTargetUnrecycle();
                    } else {
                        msg.getTarget().showMessage(0, body.getMessage());
                    }
                } else {
                    msg.getTarget().showMessage(0, body.getMessage());
                }
            }

            @Override
            protected void onFailure(ApiException exception) {
                msg.getTarget().showMessage(0, exception.getMessage());
            }

            @Override
            protected void onFinish(boolean isCanceled) {
                msg.getTarget().hideLoading();
                msg.recycle();
            }
        });
    }

    public void rxLogin(final Message msg, String username, String password) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 如果页面退出，此请求还没有得到返回信息，需要关闭连接
        if (login != null && !login.isCanceled()) {
            login.cancel();
        }
    }
}