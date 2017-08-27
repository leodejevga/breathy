package com.apps.philipps.source.helper._3D;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public abstract class SurfaceView3D extends GLSurfaceView {

    protected Renderer3D renderer;

    public SurfaceView3D(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
    }

    public SurfaceView3D(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

    }

    public void setRenderer(Renderer renderer) {
        this.renderer = (Renderer3D) renderer;
        super.setRenderer(this.renderer);
    }

}
