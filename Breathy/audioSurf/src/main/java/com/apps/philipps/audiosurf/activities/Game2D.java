package com.apps.philipps.audiosurf.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.abstracts.AbstractGameActivity;

public class Game2D extends AbstractGameActivity {

    TextView framerate;
    TextView frameCount;
    TextView dataDisplay;

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
    }
}