package me.mvp.frame.http.cookie;

import okhttp3.CookieJar;

/**
 * This interface extends {@link CookieJar} and adds methods to clear the cookies.
 */
public interface ClearableCookieJar extends CookieJar {

    /**
     * Clear all the session cookies while maintaining the persisted ones.
     */
    void clearSession();

    /**
     * Clear all the cookies from persistence and from the cache.
     */
    void clear();
}