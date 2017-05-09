package com.apps.philipps.audiosurf.activities;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.apps.philipps.audiosurf.Backend;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper._3D.Activity3D;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Game Activity
 */
public class Game extends Activity3D implements IObserver {
    MediaPlayer myMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppState.inGame = AppState.recordData = true;
        BreathData.addObserver(this);
        myMediaPlayer = MediaPlayer.create(Game.this, Backend.getDefaut_music_resource_id());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppState.inGame = AppState.recordData = false;
        myMediaPlayer.release();
    }


    @Override
    public void call(Object... messages) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        myMediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myMediaPlayer.start();
    }
}
