package com.tool.common.base.delegate;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * ActivityDelegate
 */
public interface ActivityDelegate extends Parcelable {

    String ACTIVITY_DELEGATE = "ACTIVITY_DELEGATE";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle outState);
}