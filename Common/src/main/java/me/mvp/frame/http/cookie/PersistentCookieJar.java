package me.mvp.frame.http.cookie;

import android.content.Context;

import me.mvp.frame.http.cookie.cache.CookieCache;
import me.mvp.frame.http.cookie.cache.SetCookieCache;
import me.mvp.frame.http.cookie.persistence.CookiePersistor;
import me.mvp.frame.http.cookie.persistence.SharedPrefsCookiePersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements ClearableCookieJar {

    private CookieCache cache;
    private CookiePersistor persistor;

    private CookieLoadForRequest cookieLoadForRequest;

    public PersistentCookieJar(Context context, CookieLoadForRequest cookieLoadForRequest) {
        this(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        this.cookieLoadForRequest = cookieLoadForRequest;
    }

    public PersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        this.cache = cache;
        this.persistor = persistor;

        this.cache.addAll(persistor.loadAll());
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookieLoadForRequest != null) {
            cookieLoadForRequest.saveFromResponse(url, cookies);
        }

        cache.addAll(cookies);
        persistor.saveAll(filterPersistentCookies(cookies));
    }

    private static List<Cookie> filterPersistentCookies(List<Cookie> cookies) {
        List<Cookie> persistentCookies = new ArrayList<>();

        for (Cookie cookie : cookies) {
            if (cookie.persistent()) {
                persistentCookies.add(cookie);
            }
        }
        return persistentCookies;
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookiesToRemove = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                cookiesToRemove.add(currentCookie);
                it.remove();

            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }

        persistor.removeAll(cookiesToRemove);

        if (cookieLoadForRequest != null) {
            validCookies = cookieLoadForRequest.loadForRequest(validCookies);
        }

        return validCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    synchronized public void clearSession() {
        cache.clear();
        cache.addAll(persistor.loadAll());
    }

    @Override
    synchronized public void clear() {
        cache.clear();
        persistor.clear();
    }

    /**
     * 此接口需求配合PersistentCookieJar使用
     * 这是用来从PersistentCookieJar的loadForRequest获取List<Cookie>
     * 实际上只是为了获取到接口里的Cookie值，如果项目存在两套Http模块
     * 比如登录模块用OkHttp，其他模块需要用到登录模块返回的Cookie来保持会话，此时需要实现此接口将返回的Cookie设置给另外一套Http模块
     */
    public interface CookieLoadForRequest {

        void saveFromResponse(HttpUrl url, List<Cookie> cookies);

        List<Cookie> loadForRequest(List<Cookie> cookies);

        CookieLoadForRequest EMPTY = new CookieLoadForRequest() {

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            }

            @Override
            public List<Cookie> loadForRequest(List<Cookie> cookies) {
                return cookies;
            }
        };
    }
}