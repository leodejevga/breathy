package com.apps.philipps.source.helper;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.apps.philipps.source.AppState;

/**
 * Created by Jevgenij Huebert on 11.03.2017. Project Breathy
 */

public abstract class Activity2D extends Activity {
    private static final String TAG = "Activity 2D";
    protected boolean draw;
    protected int frameRate = 0;
    protected int frame = 0;
    private boolean initialized = false;
    private long start = System.currentTimeMillis();
    private boolean destroy = false;
    private DisplayMetrics displayMetrics;
    private short ready=2;

    protected void brakeDraw(){
        draw = false;
    }

    private final Thread startToDraw =  new Thread(null, new Runnable() {

        private double millis;
        @Override
        public void run() {
            draw = true;
            long delta = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!initialized) {
                        init();
                        millis = 1000/AppState.framelimit.getLimit();
                    }
                    initialized = true;
                }
            });
            while (!destroy) {
                if(initialized){
                    delta = System.currentTimeMillis() - start;
                    if (draw && delta >= millis && ready>=2) {
                        ready = 0;
                        executeDraw(delta);
                        start = System.currentTimeMillis();
                    }
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
    }, "Activity 2D");

    protected abstract void draw();

    protected abstract void init();

    protected abstract void touch(MotionEvent event);

    protected float getScreenWidth(){
        return displayMetrics.widthPixels;
    }
    protected float getScreenHeight(){
        return displayMetrics.heightPixels;
    }

    private long sekond = System.currentTimeMillis();
    private synchronized void executeDraw(long delta){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    draw();
                    ready++;
                } catch (Exception e) {
                    Log.e(TAG, "Draw not successfull", e);
                }
                frame = ++frame;
            }
        });
        if(sekond+1000<=System.currentTimeMillis()){
            frameRate = frame;
            frame = 0;
            sekond = System.currentTimeMillis();
        }
        ready++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touch(event);
        return super.onTouchEvent(event);
    }
}
