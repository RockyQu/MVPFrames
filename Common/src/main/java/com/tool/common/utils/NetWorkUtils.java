package com.tool.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.tool.common.utils.base.BaseUtils;

/**
 * 网络状态工具类
 * <p>
 * GPRS    2G(2.5) General Packet Radia Service 114kbps
 * EDGE    2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
 * UMTS    3G WCDMA 联通3G Universal MOBILE Telecommunication System 完整的3G移动通信技术标准
 * CDMA    2G 电信 Code Division Multiple Access 码分多址
 * EVDO_0  3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
 * EVDO_A  3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
 * 1xRTT   2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
 * HSDPA   3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
 * HSUPA   3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
 * HSPA    3G (分HSDPA,HSUPA) High Speed Packet Access
 * IDEN    2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
 * EVDO_B  3G EV-DO Rev.B 14.7Mbps 下行 3.5G
 * LTE     4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
 * EHRPD   3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
 * HSPAP   3G HSPAP 比 HSDPA 快些
 */
public class NetWorkUtils extends BaseUtils {

    public NetWorkUtils() {
        super();
    }

    /**
     * 判断网络连接是否有效（此时可传输数据）。
     *
     * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
     */
    public static boolean isConnected(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    /**
     * 是否存在有效的WIFI连接
     */
    public static boolean isWifiConnected(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_WIFI && net.isConnected();
    }

    /**
     * 是否存在有效的移动连接
     *
     * @return boolean
     */
    public static boolean isMobileConnected(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE && net.isConnected();
    }

    /**
     * get connected network type by {@link ConnectivityManager}
     * <p>
     * such as WIFI, MOBILE, ETHERNET, BLUETOOTH, etc.
     *
     * @return {@link ConnectivityManager#TYPE_WIFI}, {@link ConnectivityManager#TYPE_MOBILE},
     * {@link ConnectivityManager#TYPE_ETHERNET}...
     */
    private static int getConnectedTypeINT(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        if (net != null) {
            return net.getType();
        }
        return -1;
    }

    /**
     * 获取ConnectivityManager
     */
    private static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获取TelephonyManager
     */
    private static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }
}