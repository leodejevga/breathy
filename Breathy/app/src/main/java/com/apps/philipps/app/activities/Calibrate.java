package com.apps.philipps.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.app.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.test.activities.Game;

import java.util.ArrayList;
import java.util.List;

public class Calibrate extends Activity2D {
    private List<GameObject2D> points = new ArrayList<>();

    private double minValue = Double.MAX_VALUE;
    private double maxValue = 0;
    private TextView min;
    private TextView max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
    }

    @Override
    public void call(Object... messages) {
        if (messages.length >= 1) {
            BreathData.Element element = (BreathData.Element) messages[0];
            if (minValue > element.data)
                minValue = element.data;
            if (maxValue < element.data)
                maxValue = element.data;
            min.setText("Min value: " + minValue);
            max.setText("Max value: " + maxValue);
            Log.e(TAG + " Calibrate", "Height " + getScreenHeight() + "\n ScreenHeight " + getScreenHeight(true));
            double y = (element.data-minValue)/ maxValue * (getScreenHeight()-50) + 25;
            Log.e(TAG + " Calibrate", "y " + y);
            Vector position = new Vector(getScreenWidth(), y);
            Vector destination = new Vector(-25, y);
            points.add(initObject(R.drawable.point, position, destination, 200*SCREEN_FACTOR));
        }
    }

    @Override
    protected void draw() {
        List<GameObject2D> toRemove = new ArrayList<>();
        for(GameObject2D point : points){
            point.update(delta);
            if(!point.isMoving()) {
                toRemove.add(point);
                game.removeView(point.getView());
            }
        }
        points.removeAll(toRemove);
    }

    @Override
    protected void init() {
        game = (RelativeLayout) findViewById(R.id.data_view);
        min = (TextView) findViewById(R.id.data_min);
        max = (TextView) findViewById(R.id.data_max);
        BreathData.addObserver(this);
    }

    @Override
    protected void touch(MotionEvent event) {

    }

    public void accept(View view) {
        AppState.breathyUserMin = minValue;
        AppState.breathyUserMax = maxValue;
        finish();
    }
}
