package com.apps.philipps.opengltest.activities;

import android.content.Context;

import com.apps.philipps.opengltest.GameEngine;
import com.apps.philipps.source.helper._3D.Renderer3D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class MyGLRenderer extends Renderer3D {
    private Context mActivityContext;
    public boolean isDrawing = false;


    public GameEngine gameEngine;

    public MyGLRenderer(Context context) {
        this.mActivityContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        super.onSurfaceCreated(unused, config);
        gameEngine = new GameEngine(mActivityContext, this);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        isDrawing = true;
        super.onDrawFrame(unused);
        gameEngine.runGame(deltaTime);
        isDrawing=false;
    }
}