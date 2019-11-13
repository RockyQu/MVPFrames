package me.mvp.frame.utils

import com.google.gson.Gson

import me.mvp.frame.di.component.AppComponent
import me.mvp.frame.utils.base.BaseUtils

/**
 * Gson 工具类
 */
class GsonUtils : BaseUtils() {

    companion object {

        private var gson: Gson = Gson()

        fun getGson(component: AppComponent): Gson {
            return component.gson
        }

        /**
         * JsonString to Entity
         *
         * @param value
         * @param cls
         */
        fun <T> getEntity(value: String, cls: Class<T>): T? {
            var t: T? = null
            try {
                t = gson.fromJson(value, cls)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return t
        }

        /**
         * Object to JsonString
         *
         * @param src
         */
        fun getString(src: Any): String? {
            var value: String? = null
            try {
                value = gson.toJson(src)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return value
        }
    }
}