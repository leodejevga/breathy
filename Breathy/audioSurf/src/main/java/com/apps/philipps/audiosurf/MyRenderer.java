package com.apps.philipps.audiosurf;

import android.content.Context;

import com.apps.philipps.audiosurf.activities.Game;
import com.apps.philipps.source.helper._3D.GameObject3D;
import com.apps.philipps.source.helper._3D.Renderer3D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */
public class MyRenderer extends Renderer3D {
    private int frames;
    private Context game;
    private GameObject3D triangle;
    public MyRenderer(Game game) {
        this.game = game;
        this.triangle = new GameObject3D(new Shapes.Triangle());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        triangle.update(deltaTime);
    }
}
