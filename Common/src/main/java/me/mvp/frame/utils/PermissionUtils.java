package me.mvp.frame.utils;

import android.Manifest;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import me.logg.Logg;
import me.mvp.frame.utils.base.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

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

    public PermissionUtils() {
        super();
    }

    public interface RequestPermission {

        /**
         * 权限申请成功执行的方法
         */
        void onRequestPermissionSuccess();

        /**
         * 权限申请失败执行的方法
         * 包括：用户拒绝、用户选择了禁止弹出、当批量申请只要有一个拒绝就会执行该方法
         */
        void onRequestPermissionFailure();
    }

    public static void requestPermissions(final RequestPermission requestPermission, RxPermissions rxPermissions, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }

        Logg.e(permissions);
        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) { // 过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        Logg.e(needRequest);
        int count = needRequest.size();
        if (count == 0) {// 全部权限已经申请过，直接执行成功操作
            requestPermission.onRequestPermissionSuccess();
        } else {
            rxPermissions
                    .requestEach(needRequest.toArray(new String[count]))
                    .buffer(count)
                    .subscribe(new Consumer<List<Permission>>() {
                        @Override
                        public void accept(List<Permission> permissions) throws Exception {
                            for (Permission p : permissions) {
                                Logg.e("Permission " + p.name + " " + p.granted);
                                if (!p.granted) {
                                    if (p.shouldShowRequestPermissionRationale) {
                                        Logg.e("Request permissions failure");
                                        requestPermission.onRequestPermissionFailure();
                                        return;
                                    } else {
                                        Logg.e("Request permissions failure with ask never again");
                                        requestPermission.onRequestPermissionFailure();
                                        return;
                                    }
                                }
                            }

                            Logg.e("Request permissions success");
                            requestPermission.onRequestPermissionSuccess();
                        }
                    });
        }
    }

    public static void writeContacts(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.WRITE_CONTACTS);
    }

    public static void getAccounts(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.GET_ACCOUNTS);
    }

    public static void readContacts(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.READ_CONTACTS);
    }

    public static void readPhoneState(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.READ_PHONE_STATE);
    }

    public static void callPhone(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.CALL_PHONE);
    }

    public static void readCalendar(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.READ_CALENDAR);
    }

    public static void writeCalendar(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.WRITE_CALENDAR);
    }

    public static void camera(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    public static void location(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static void writeExternalStorage(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void recordAudio(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.RECORD_AUDIO);
    }

    public static void readSMS(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.READ_SMS);
    }

    public static void sendSMS(RequestPermission requestPermission, RxPermissions rxPermissions) {
        requestPermissions(requestPermission, rxPermissions, Manifest.permission.SEND_SMS);
    }
}