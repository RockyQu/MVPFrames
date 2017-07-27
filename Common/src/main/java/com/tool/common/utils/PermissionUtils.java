package com.tool.common.utils;

import android.Manifest;

import com.tool.common.utils.base.BaseUtils;

/**
 * 权限管理
 * <p>
 * Google将权限分为两类：1、Normal Permissions，2、Dangerous Permission
 * <p>
 * 1、Normal Permissions
 * 普通权限在AndroidManifest.xml申请
 * <p>
 * 2、Dangerous Permissions
 * 通过adb shell pm list permissions -d -g进行查看
 * <p>
 * group:android.permission-group.CONTACTS
 * permission:android.permission.WRITE_CONTACTS
 * permission:android.permission.GET_ACCOUNTS
 * permission:android.permission.READ_CONTACTS
 * <p>
 * group:android.permission-group.OTHER
 * <p>
 * group:android.permission-group.PHONE
 * permission:android.permission.READ_CALL_LOG
 * permission:android.permission.READ_PHONE_STATE
 * permission:android.permission.CALL_PHONE
 * permission:android.permission.WRITE_CALL_LOG
 * permission:android.permission.USE_SIP
 * permission:android.permission.PROCESS_OUTGOING_CALLS
 * permission:com.android.voicemail.permission.ADD_VOICEMAIL
 * <p>
 * group:android.permission-group.CALENDAR
 * permission:android.permission.READ_CALENDAR
 * permission:android.permission.WRITE_CALENDAR
 * <p>
 * group:android.permission-group.CAMERA
 * permission:android.permission.CAMERA
 * <p>
 * group:android.permission-group.SENSORS
 * permission:android.permission.BODY_SENSORS
 * <p>
 * group:android.permission-group.LOCATION
 * permission:android.permission.ACCESS_FINE_LOCATION
 * permission:android.permission.ACCESS_COARSE_LOCATION
 * <p>
 * group:android.permission-group.STORAGE
 * permission:android.permission.READ_EXTERNAL_STORAGE
 * permission:android.permission.WRITE_EXTERNAL_STORAGE
 * <p>
 * group:android.permission-group.MICROPHONE
 * permission:android.permission.RECORD_AUDIO
 * <p>
 * group:android.permission-group.SMS
 * permission:android.permission.READ_SMS
 * permission:android.permission.RECEIVE_WAP_PUSH
 * permission:android.permission.RECEIVE_MMS
 * permission:android.permission.RECEIVE_SMS
 * permission:android.permission.SEND_SMS
 * permission:android.permission.READ_CELL_BROADCASTS
 */
public class PermissionUtils extends BaseUtils {

    // 允许程序访问电话状态
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    // 允许访问照相机
    public static final String CAMERA = Manifest.permission.CAMERA;
    // 通过GPS芯片接收卫星的定位信息
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // 允许程序写入外部存储，如SD卡上写文件
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    // 允许程序录制音频
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    // 允许程序读取短信息
    public static final String READ_SMS = Manifest.permission.READ_SMS;
    // 允许程序写入但不读取用户联系人数据
    public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;

    // 常用权限组，用于批量申请
    public static final String[] PERMISSIONS = {READ_PHONE_STATE, CAMERA, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE, RECORD_AUDIO, READ_SMS, WRITE_CONTACTS};

    // requestCode
    public static final int PERMISSION_CODE = 100;

    public PermissionUtils() {
        super();
    }

}