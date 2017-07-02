package com.breathy.racing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.breathy.racing.R;

public class Game extends Activity2D {


    private TextView highscore;
    private TextView dataDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racing_game);
        AppState.recordData = AppState.inGame = true;
    }


    @Override
    protected void draw() {
        String data = BreathData.getAsString(0, 5);
        dataDisplay.setText(data);

        highscore.setText("frame " + frame);
    }

    @Override
    protected void init() {
        dataDisplay = (TextView) findViewById(R.id.test_data);
        highscore = (TextView) findViewById(R.id.RacingScore);
    }

    @Override
    protected void touch(MotionEvent event) {

    }

    public void startGame(View view) {
        Intent i = new Intent(this, Application.class);
        startActivity(i);
    }

    @Override
    public void call(Object... messages) {

    }
}