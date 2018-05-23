package me.mvp.demo.app.config;

import android.content.Context;

import java.security.cert.CertificateException;

import javax.net.ssl.X509TrustManager;

import me.mvp.frame.di.module.HttpModule;
import okhttp3.OkHttpClient;

/**
 * 扩展自定义配置 OkHttp 参数
 */
public class OkHttpConfig implements HttpModule.OkHttpConfiguration {

    final X509TrustManager trustAllCert =
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };

    @Override
    public void configOkHttp(Context context, OkHttpClient.Builder builder) {
        // 配置 HTTPS
        builder.sslSocketFactory(new SSLSocketFactoryCompat(trustAllCert), trustAllCert);
        // builder.hostnameVerifier(new TrustAllHostnameVerifier());
    }
}