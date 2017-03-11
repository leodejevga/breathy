package com.apps.philipps.source.abstracts;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.interfaces.IObserver;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */

public abstract class AbstractRenderer implements GLSurfaceView.Renderer {

    protected IObserver observer;
    private long start = System.currentTimeMillis();
    private double framerate;


    @Override
    public void onDrawFrame(GL10 gl) {
        long delta = System.currentTimeMillis() - start;
        if(delta< (1000/ AppState.framelimit.getLimit()))
            try {
                Thread.sleep((int)(1000/AppState.framelimit.getLimit() - delta-1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        delta = (System.currentTimeMillis() - start);
        framerate = 1000/delta;
        Log.d("Delta", "ms = " + delta);
        start = System.currentTimeMillis();
        observer.call("Draw");
    }

    public double getFramerate() {
        return framerate;
    }
}