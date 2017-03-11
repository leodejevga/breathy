package com.apps.philipps.source.abstracts;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */
public abstract class AbstractGlActivity extends Activity {

    /**
     * The Open gl.
     */
    protected GLSurfaceView openGL;
    /**
     * The Game renderer.
     */
    protected GLSurfaceView.Renderer gameRenderer;

    /**
     * Please initialize here your gameRenderer
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        openGL = new GLSurfaceView(this);
        openGL.setRenderer(gameRenderer);
        this.setContentView(openGL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        openGL.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        openGL.onResume();
    }
}
