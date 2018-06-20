package me.mvp.frame.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http 通信拦截器接口
 */
public interface NetworkInterceptorHandler {

    /**
     * Http 请求，这里可以添加、删除或替换请求头信息，还可以改变的请求携带的实体
     *
     * @param chain
     * @param request
     * @return Request
     */
    Request onHttpRequest(Interceptor.Chain chain, Request request);

    /**
     * Http 响应，这里提前拿到 Http 响应结果，可以用来判断 Token 是否过期等
     *
     * @param result
     * @param chain
     * @param response
     * @return Response
     */
    Response onHttpResponse(String result, Interceptor.Chain chain, Request request, Response response);

    NetworkInterceptorHandler EMPTY = new NetworkInterceptorHandler() {

        @Override
        public Request onHttpRequest(Interceptor.Chain chain, Request request) {
            return request;
        }

        @Override
        public Response onHttpResponse(String result, Interceptor.Chain chain, Request request, Response response) {
            return response;
        }
    };
}