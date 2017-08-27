package com.apps.philipps.source.helper._2D;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Created by Jevgenij Huebert on 11.03.2017. Project Breathy
 */

public abstract class Activity2D extends Activity implements IObserver {
    protected final float SCREEN_FACTOR = (getScreenHeight()+getScreenWidth()) / (1080+1920);

    private static final String TAG = "Activity 2D";
    protected boolean draw;
    protected int frameRate = 0;
    protected int frame = 0;
    private boolean initialized = false;
    private long start = System.currentTimeMillis();
    private short ready=2;
    private static int thread = 0;

    protected void brakeDraw(){
        draw = false;
    }


    private final Runnable drawing =  new Runnable() {
        private double millis;
        @Override
        public void run() {
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
            while (draw) {
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
    };

    private void startoDraw(){
        draw = true;
        Thread drawThread = new Thread(null, drawing, "Activity 2D " + thread++);
        drawThread.setPriority(Thread.MAX_PRIORITY);
        drawThread.start();
    }

    protected abstract void draw();

    protected abstract void init();

    protected abstract void touch(MotionEvent event);

    protected float getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    protected float getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private long sekond = System.currentTimeMillis();

    private synchronized void executeDraw(long delta){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    PlanManager.update();
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
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "OnPause");
        AppState.recordData = AppState.inGame = false;
        BreathData.removeObserver(this);
        BreathData.saveRest();
        draw = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume");
        AppState.recordData = AppState.inGame = true;
        BreathData.addObserver(this);
        startoDraw();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touch(event);
        return super.onTouchEvent(event);
    }
}
