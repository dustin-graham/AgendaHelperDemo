package com.rain.android.agenda.util;

import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: dusting
 * Date: 6/19/13
 * Time: 2:45 PM
 */
public interface LifecyclePlugin {
    void onCreate();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
    void onLifecyclePluginSavedInstanceState(Bundle outState);
}
