package me.mvp.demo.app.config;

import me.mvp.frame.http.NetworkInterceptorHandler;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http全局响应结果的处理类
 */
public class NetworkInterceptorConfig implements NetworkInterceptorHandler {

    @Override
    public Request onHttpRequest(Interceptor.Chain chain, Request request) {
        // 请求服务器之前可以拿到Request,做一些操作比如给Request统一添加Token或者Header，不做任务操作则直接返回Request
        // return chain.request().newBuilder().header("token", tokenId).build();
        return request;
    }

    @Override
    public Response onHttpResponse(String result, Interceptor.Chain chain, Request request, Response response) {
        // 这里提前拿到Http请求结果,可以用来检测，如Token是否过期
//                        if (!TextUtils.isEmpty(result)) {
//                            ResponseEntity entity = GsonUtils.getEntity(result, ResponseEntity.class);
//
//                            // 登录超时或授权失效，重新获取授权
//                            if (entity.getCode() == -253 || entity.getCode() == -254) {
//                                AppComponent component = ((App) context).getAppComponent();
//
//                                // 读取当前登录用户信息
//                                String value = PreferencesUtils.getString(context, LoginActivity.class.getName(), null);
//                                if (value != null) {
//                                    User user = GsonUtils.getEntity(value, User.class);
//
//                                    Call<ResponseEntity<JsonObject>> apiToken = component.getRepositoryManager().obtainApiService(ApiCommon.class).getToken("CF2QCXW6", DeviceUtils.getIMEI(context), user.getUserId());
//                                    try {
//                                        JsonObject resultToken = apiToken.execute().body().getData();
//                                        apiToken.execute().body();
////                                        Logg.e(resultToken);
////                                            Logg.e(resultToken.get("access_token"));
//                                        String token = resultToken.get("access_token").getAsString();
////                                            Logg.e(token);
//
//                                        HttpUrl.Builder modifiedUrl = request.url().newBuilder()
//                                                .scheme(request.url().scheme())
//                                                .host(request.url().host());
//
//                                        modifiedUrl.addQueryParameter("access_token", token);
//
//                                        Request newRequest = request.newBuilder()
////                                                .method(request.method(), request.body())
////                                                .url(modifiedUrl.build())
//                                                .build();
//                                        response.body().close();
//                                        return chain.proceed(newRequest);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }

        return response;
    }
}