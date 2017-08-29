package com.tool.common.http;

import okhttp3.HttpUrl;

public interface BaseUrl {

    /**
     * 针对于BaseUrl在App启动时不能确定，需要请求服务器接口动态获取的应用场景
     * 在调用Retrofit接口之前，使用Okhttp或其他方式,请求到正确的BaseUrl并通过此方法返回
     * @return
     */
    HttpUrl url();
}