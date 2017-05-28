package com.apps.philipps.opengltest.activities;

import android.os.Bundle;

import com.apps.philipps.source.helper._3D.Activity3D;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class TGame extends Activity3D {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openGL = new MyGLSurfaceView(this, new MyGLRenderer(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        openGL.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        openGL.onResume();
    }
}
