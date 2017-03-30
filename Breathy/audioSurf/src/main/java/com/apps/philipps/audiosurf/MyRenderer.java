package com.apps.philipps.audiosurf;

import android.content.Context;

import com.apps.philipps.audiosurf.activities.Game;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.helper.Camera3D;
import com.apps.philipps.source.helper.GameObject3D;
import com.apps.philipps.source.helper.Renderer3D;
import com.apps.philipps.source.interfaces.IObserver;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */
public class MyRenderer extends Renderer3D {
    private int frames;
    private Context game;
    private GameObject3D triangle;
    public MyRenderer(Game game, IObserver observer) {
        this.observer = observer;
        this.game = game;
        this.triangle = new GameObject3D(new Shapes.Triangle());
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        observer.call("update the renderer");
        triangle.update(deltaTime);
    }
}
