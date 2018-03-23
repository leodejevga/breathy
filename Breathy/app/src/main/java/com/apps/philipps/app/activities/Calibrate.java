package com.apps.philipps.app.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.app.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper.Animation;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;

public class Calibrate extends Activity2D {

    private double minValue;
    private double maxValue;
    private TextView min;
    private TextView max;
    private TextView calibrateInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
        AppState.inGame = false;
    }

    @Override
    public void call(Object... messages) {
        if (messages.length >= 1 && isInitialized()) {
            double data = ((BreathData.Element) messages[0]).data;
            minValue = Double.MAX_VALUE;
            maxValue = 0;
            if (minValue > data)
                minValue = data;
            if (maxValue < data)
                maxValue = data;
            for (POINT o : Animation.get(POINT.class)) {
                double y = o.data;
                if (minValue > y)
                    minValue = y;
                if (maxValue < y)
                    maxValue = y;
            }
            min.setText(getString(R.string.min) + ": " + minValue);
            max.setText(getString(R.string.min) + ": " + maxValue);
            new POINT(data);
        }
    }

    @Override
    protected void onLoading(boolean firstLoad, int progress, long delta) {
    }

    @Override
    protected void draw() {
        calibrateInformation.setText("Framerate " + getFrameRate());
        Animation.updateAnimations(delta);
    }

    @Override
    protected void init() {
        game = (RelativeLayout) findViewById(R.id.data_view);
        min = (TextView) findViewById(R.id.data_min);
        max = (TextView) findViewById(R.id.data_max);
        calibrateInformation = (TextView) findViewById(R.id.calibrateInformation);
    }

    @Override
    protected void touch(MotionEvent event) {

    }

    public void accept(View view) {
        AppState.breathyUserMin = minValue;
        AppState.breathyUserMax = maxValue;
        finish();
    }

    private class POINT extends Animation {
        GameObject2D o;
        double data;

        public POINT(double data) {
            super(1);
            Vector position = new Vector(Calibrate.this.getScreenWidth(), data);
            Vector destination = new Vector(-50, data);
            o = new GameObject2D(Calibrate.this, position, destination);
            ImageView i = o.getView();
            i.setImageResource(R.drawable.point);
            game.addView(i);
            o.move(100);

            this.data = data;
        }

        @Override
        protected void update(double delta) {
            if (!o.isMoving()) {
                remove();
            }
            else {
                double y = 0;
                if (Math.abs(minValue - maxValue) <= AppState.MAX_BT_VALUE / 50)
                    y = data - AppState.breathyNormState + getScreenHeight() / 2;
                else y = (data - minValue) / (maxValue - minValue) * (getScreenHeight() - 50);
                o.setPosition(new Vector(o.getPosition().get(0), y));
                o.move(new Vector(-50, y));
                o.update(delta);
            }
        }

        @Override
        public void remove() {
            super.remove();
            game.removeView(o.getView());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppState.recordData = false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
