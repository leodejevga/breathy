package com.apps.philipps.audiosurf.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper.Activity2D;
import com.apps.philipps.source.interfaces.IObserver;

public class Game2D extends Activity2D {

    private TextView framerate;
    private TextView frameCount;
    private TextView dataDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as_game2d);
        AppState.recordData = AppState.inGame = true;
    }


    @Override
    protected void draw() {
        String data = BreathData.getAsString(0, 5);
        dataDisplay.setText(data);
        framerate.setText(frameRate + " fps");
        frameCount.setText("frame " + frame);
    }

    @Override
    protected void init() {
        framerate = (TextView) findViewById(R.id.as_framerate);
        dataDisplay = (TextView) findViewById(R.id.as_data);
        frameCount = (TextView) findViewById(R.id.as_frameCount);

        AppState.framelimit = AppState.Framelimit.Thirty;
    }

    @Override
    protected void touched(MotionEvent event) {

    }

    public void startGame(View view) {
        Intent i = new Intent(this, Application.class);
        startActivity(i);
    }
}