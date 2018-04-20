package me.mvp.frame.http.cookie.persistence;

import java.util.Collection;
import java.util.List;

import okhttp3.Cookie;

/**
 * A CookiePersistor handles the persistent cookie storage.
 */
public interface CookiePersistor {

    List<Cookie> loadAll();

    /**
     * Persist all cookies, existing cookies will be overwritten.
     *
     * @param cookies cookies persist
     */
    void saveAll(Collection<Cookie> cookies);

    /**
     * Removes indicated cookies from persistence.
     *
     * @param cookies cookies to remove from persistence
     */
    void removeAll(Collection<Cookie> cookies);

    /**
     * Clear all cookies from persistence.
     */
    void clear();

}