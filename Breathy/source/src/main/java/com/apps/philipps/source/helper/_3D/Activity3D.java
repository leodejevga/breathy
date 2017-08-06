package com.apps.philipps.source.helper._3D;

import android.app.Activity;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */
public abstract class Activity3D extends Activity {

    /**
     * The Open gl.
     */
    protected SurfaceView3D openGL;

    @Override
    protected void onPause() {
        super.onPause();
        if(openGL!=null)
            openGL.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(openGL!=null)
            openGL.onResume();
    }
}
