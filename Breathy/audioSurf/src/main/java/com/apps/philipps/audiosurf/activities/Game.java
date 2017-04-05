package com.apps.philipps.audiosurf.activities;

import android.os.Bundle;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper._3D.Activity3D;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Game Activity
 */
public class Game extends Activity3D implements IObserver {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppState.inGame = AppState.recordData = true;
        BreathData.addObserver(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppState.inGame = AppState.recordData = false;
    }


    @Override
    public void call(Object... messages) {
    }

}
