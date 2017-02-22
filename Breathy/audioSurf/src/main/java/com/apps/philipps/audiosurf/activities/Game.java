package com.apps.philipps.audiosurf.activities;

import android.app.Activity;
import android.os.Bundle;

import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.AppState;

/**
 * Game Activity
 */
public class Game extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as_game);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppState.inGame = AppState.recordData = false;
    }
    //TODO: Hier wird das Spiel ausgeführt. OpenGL.
}
