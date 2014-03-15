package com.rain.android.agenda.activity;

import android.support.v4.app.FragmentActivity;

import com.rain.android.agenda.util.ScopedBus;

/**
 * Created by dustin on 3/13/14. :)
 * taken from Jake Wharton's gist: https://gist.github.com/JakeWharton/3057437
 */
public class BaseActivity extends FragmentActivity {
    private final ScopedBus scopedBus = new ScopedBus();

    protected ScopedBus getBus() {
        return scopedBus;
    }

    @Override
    public void onPause() {
        super.onPause();
        scopedBus.paused();
    }

    @Override
    public void onResume() {
        super.onResume();
        scopedBus.resumed();
    }
}
