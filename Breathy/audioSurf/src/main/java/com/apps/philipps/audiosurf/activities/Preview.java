package com.apps.philipps.audiosurf.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.apps.philipps.audiosurf.R;

/**
 * Preview Activity
 */
public class Preview extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as_preview);
    }
}
