package me.mvp.frame.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.MemoryInfo
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.ActivityManager.RunningServiceInfo
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Message
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

import me.mvp.frame.base.App
import me.mvp.frame.di.component.AppComponent
import me.mvp.frame.utils.base.BaseUtils

import org.simple.eventbus.EventBus

import java.io.File

import me.mvp.frame.integration.AppManager.APPMANAGER_MESSAGE
import me.mvp.frame.integration.AppManager.EXIT
import me.mvp.frame.integration.AppManager.START_ACTIVITY

/**
 * 应用工具类
 */
class AppUtils : BaseUtils() {

    companion object {

        /**
         * 获取版本号
         *
         * @param context
         * @return int
         */
        fun getVersionCode(context: Context): Int {
            var versionCode:Int
            try {
                versionCode = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (ex: PackageManager.NameNotFoundException) {
                versionCode = 0
            }

            return versionCode
        }

        /**
         * 获取指定包名应用的版本号
         *
         * @param context
         * @param packageName
         * @return int
         */
        fun getVersionCode(context: Context, packageName: String): Int {
            var versionCode = 0
            try {
                versionCode = context.packageManager.getPackageInfo(packageName, 0).versionCode
            } catch (ex: PackageManager.NameNotFoundException) {
                versionCode = 0
            }

            return versionCode
        }

        /**
         * 获取版本名
         *
         * @param context
         * @return String
         */
        fun getVersionName(context: Context): String {
            var name: String
            try {
                name = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (ex: PackageManager.NameNotFoundException) {
                name = ""
            }

            return name
        }

        /**
         * 获取应用渠道信息
         *
         * @param context
         * @param channel 渠道名称
         * @return String
         */
        fun getAppChannel(context: Context, channel: String): String {
            try {
                val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                if (appInfo.metaData != null) {
                    for (key in appInfo.metaData.keySet()) {
                        if (key == channel) {
                            return appInfo.metaData.get(key)!!.toString()
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return channel
        }

        /**
         * 获取应用运行的最大内存
         *
         * @return 最大内存
         */
        val maxMemory: Long
            get() = Runtime.getRuntime().maxMemory() / 1024

        /**
         * 检测服务是否运行
         *
         * @param context   上下文
         * @param className 类名
         * @return 是否运行的状态
         */
        fun isServiceRunning(context: Context, className: String): Boolean {
            var isRunning = false
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val servicesList = activityManager.getRunningServices(Integer.MAX_VALUE)
            for (si in servicesList) {
                if (className == si.service.className) {
                    isRunning = true
                }
            }
            return isRunning
        }

        /**
         * 停止运行服务
         *
         * @param context 上下文
         * @param cls     类名
         */
        fun startService(context: Context, cls: Class<*>) {
            var intentService: Intent? = null
            try {
                intentService = Intent(context, cls)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (intentService != null) {
                context.startService(intentService)
            }
        }

        /**
         * 停止运行服务
         *
         * @param context 上下文
         * @param cls     类名
         * @return 是否执行成功
         */
        fun stopService(context: Context, cls: Class<*>): Boolean {
            var intentService: Intent? = null
            var ret = false
            try {
                intentService = Intent(context, cls)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (intentService != null) {
                ret = context.stopService(intentService)
            }
            return ret
        }

        /**
         * 给定Context获取进程名
         *
         * @param context
         * @return String
         */
        fun getProcessName(context: Context): String? {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses ?: return null
            for (proInfo in runningApps) {
                if (proInfo.pid == android.os.Process.myPid()) {
                    if (proInfo.processName != null) {
                        return proInfo.processName
                    }
                }
            }
            return null
        }

        /**
         * 获取设备的可用内存大小
         *
         * @param context 应用上下文对象context
         * @return 当前内存大小
         */
        fun getDeviceUsableMemory(context: Context): Int {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val mi = MemoryInfo()
            am.getMemoryInfo(mi)
            // 返回当前系统的可用内存
            return (mi.availMem / (1024 * 1024)).toInt()
        }

        /**
         * 获取activity尺寸
         *
         * @param activity
         * @return int[]
         */
        fun getRealScreenSize(activity: Activity): IntArray {
            val size = IntArray(2)
            var screenWidth = 0
            var screenHeight = 0
            val w = activity.windowManager
            val d = w.defaultDisplay
            val metrics = DisplayMetrics()
            d.getMetrics(metrics)
            // since SDK_INT = 1;
            screenWidth = metrics.widthPixels
            screenHeight = metrics.heightPixels
            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
                try {
                    screenWidth = Display::class.java.getMethod("getRawWidth")
                            .invoke(d) as Int
                    screenHeight = Display::class.java
                            .getMethod("getRawHeight").invoke(d) as Int
                } catch (ignored: Exception) {
                }

            }
            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17) {
                try {
                    val realSize = Point()
                    Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
                    screenWidth = realSize.x
                    screenHeight = realSize.y
                } catch (ignored: Exception) {
                }

            }
            size[0] = screenWidth
            size[1] = screenHeight
            return size
        }

        /**
         * 拍照
         *
         * @param activity Activity
         * @param path     保存路径
         */
        fun openCamera(activity: Activity, path: String, requestCode: Int) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(path)))
            activity.startActivityForResult(intent, requestCode)
        }

        /**
         * 本地相册
         *
         * @param activity Activity
         */
        fun openLocal(activity: Activity, requestCode: Int) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activity.startActivityForResult(intent, requestCode)
        }

        /**
         * 拨打电话
         *
         * @param activity Activity
         * @param phone    电话号码
         */
        fun call(activity: Activity, phone: String) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
            activity.startActivity(intent)
        }

        /**
         * 打开网页
         *
         * @param activity Activity
         * @param url      URL
         */
        fun openHtml(activity: Activity, url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(intent)
        }

        /**
         * 发送短信
         *
         * @param activity Activity
         * @param number   电话号码
         * @param message  短信内容
         */
        fun sendMessage(activity: Activity, number: String, message: String) {
            val uri = Uri.parse("smsto:$number")
            val sendIntent = Intent(Intent.ACTION_VIEW, uri)
            sendIntent.putExtra("sms_body", message)
            activity.startActivity(sendIntent)
        }

        /**
         * 跳转到权限设置界面
         */
        fun applicationDetailsSettings(context: Context) {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", context.packageName, null)
            startActivity(intent)
        }

        /**
         * 通过EventBus远程遥控跳转页面
         *
         * @param cls
         */
        fun startActivity(cls: Class<*>) {
            val message = Message()
            message.what = START_ACTIVITY
            message.obj = cls
            EventBus.getDefault().post(message, APPMANAGER_MESSAGE)
        }

        /**
         * 通过EventBus远程遥控跳转页面
         *
         * @param content
         */
        fun startActivity(content: Intent) {
            val message = Message()
            message.what = START_ACTIVITY
            message.obj = content
            EventBus.getDefault().post(message, APPMANAGER_MESSAGE)
        }

        fun exit() {
            val message = Message()
            message.what = EXIT
            EventBus.getDefault().post(message, APPMANAGER_MESSAGE)
        }

        fun obtainAppComponentFromContext(context: Context): AppComponent {
            Preconditions.checkState(context.applicationContext is App, "Application does not implements App")
            return (context.applicationContext as App).appComponent
        }
    }
}