package me.mvp.frame.base.delegate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * {@link Activity} 生命周期代理接口
 */
public interface ActivityDelegate{

    String ACTIVITY_DELEGATE = "ACTIVITY_DELEGATE";

    void onCreate(@NonNull Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(@NonNull Bundle outState);

    void onRestoreInstanceState(@NonNull Bundle outState);
}