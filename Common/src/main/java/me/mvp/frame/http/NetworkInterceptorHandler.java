package me.mvp.frame.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http 通信拦截器
 */
public interface NetworkInterceptorHandler {

    /**
     * Http请求
     *
     * @param chain
     * @param request
     * @return Request
     */
    Request onHttpRequest(Interceptor.Chain chain, Request request);

    /**
     * Http响应，这里提前拿到http响应结果,可以用来判断Token是否过期
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