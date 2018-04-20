package me.mvp.frame.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

/**
 * FragmentDelegate
 */
public interface FragmentDelegate extends Parcelable {

    String FRAGMENT_DELEGATE = "FRAGMENT_DELEGATE";

    void onAttach(Context context);

    void onCreate(Bundle savedInstanceState);

    void onCreateView(View view, Bundle savedInstanceState);

    void onActivityCreated(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroyView();

    void onDestroy();

    void onDetach();

    void onSaveInstanceState(Bundle outState);
}