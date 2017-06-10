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


    public GameEngine gameEngine;

    public MyGLRenderer(Context context) {
        this.mActivityContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        super.onSurfaceCreated(unused, config);
        gameEngine = new GameEngine(mActivityContext);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        super.onDrawFrame(unused);
        refreshCameraPosition();
        Renderer3D.light.setUpLight();

        gameEngine.drawStreet(deltaTime);
        gameEngine.runSimulation(deltaTime);
        Renderer3D.light.drawLight();
    }


    private void refreshCameraPosition() {
        //Renderer3D.camera3D.move(new Vector(), new Vector(), new Vector(), new Vector(1, 0, 0, angle));
    }
}