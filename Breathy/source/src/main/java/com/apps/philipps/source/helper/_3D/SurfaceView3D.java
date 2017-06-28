package com.apps.philipps.source.helper._3D;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public abstract class SurfaceView3D extends GLSurfaceView {

    protected Renderer3D renderer;
    public SurfaceView3D(Context context, Renderer3D renderer) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        this.renderer = renderer;
        setRenderer(this.renderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
