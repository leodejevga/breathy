package com.apps.philipps.source.helper;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.apps.philipps.source.AppState;

/**
 * Created by Jevgenij Huebert on 11.03.2017. Project Breathy
 */

public abstract class Activity2D extends Activity {
    private static final String TAG = "Game Activity";
    protected boolean draw;
    protected int frameRate = 0;
    protected int frame = 0;
    private boolean initialized = false;
    private long start = System.currentTimeMillis();


    private final Thread startToDraw = new Thread(new Runnable() {

        @Override
        public void run() {
            draw = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!initialized) {
                        init();
                    }
                    initialized = true;
                }
            });
            while (draw) {
                if (System.currentTimeMillis() - start >= 1000 / AppState.framelimit.getLimit()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            draw();
                            frame = ++frame%AppState.framelimit.getLimit();
                        }
                    });
                    long temp = System.currentTimeMillis() - start;
                    frameRate = (int)(1000 / temp);
                    start = System.currentTimeMillis();
                }
            }
        }
    });

    protected abstract void draw();

    protected abstract void init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startToDraw.start();
        AppState.framelimit = AppState.Framelimit.HundredTwenty;
        draw = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        draw = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        draw = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!startToDraw.isAlive())
            startToDraw.start();
    }
}
