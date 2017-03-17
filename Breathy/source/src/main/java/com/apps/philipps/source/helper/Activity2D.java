package com.apps.philipps.source.helper;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
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
    private boolean destroy = false;
    private DisplayMetrics displayMetrics;

    protected void brakeDraw(){
        draw = false;
    }

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
            while (!destroy) {
                long delta = System.currentTimeMillis() - start;
                if (initialized && draw && delta >= 1000/AppState.framelimit.getLimit()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                draw();
                            } catch (Exception e) {
                                Log.d(TAG, "Draw not successfull");
                            }
                            frame = ++frame % AppState.framelimit.getLimit();
                        }
                    });
                    long temp = System.currentTimeMillis() - start;
                    if(temp == 0)
                        frameRate = Integer.MAX_VALUE;
                    else
                        frameRate = (int)(1000 / temp);
                    start = System.currentTimeMillis();
                }
                else{
                    try {
                        long sleep = 1000/AppState.framelimit.getLimit() - delta;
                        Thread.sleep(sleep>10?sleep-3:0);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Fail to wait");
                    }
                }
            }
        }
    });

    protected abstract void draw();

    protected abstract void init();

    protected int getScreenWidth(){
        return displayMetrics.widthPixels;
    }
    protected int getScreenHeight(){
        return displayMetrics.heightPixels;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        startToDraw.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        draw = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        draw = true;
    }
}
