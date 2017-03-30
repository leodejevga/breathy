package com.apps.philipps.source.helper;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.interfaces.IObserver;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */

public abstract class Renderer3D implements GLSurfaceView.Renderer {

    protected IObserver observer;
    private long start = System.currentTimeMillis();
    private double frameRate;
    protected long deltaTime;

    public static Camera3D camera3D;

    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if(camera3D==null)
            camera3D = new Camera3D(width, height);
        else camera3D.changeSurface(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        deltaTime = System.currentTimeMillis() - start;
        if(deltaTime <= (1000/ AppState.framelimit.getLimit()))
            try {
                int millis = (int)(1000/AppState.framelimit.getLimit()-1 - deltaTime);
                Thread.sleep(millis>0?millis:0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        deltaTime = (System.currentTimeMillis() - start);
        frameRate = 1000/deltaTime;
        start = System.currentTimeMillis();
        observer.call("Draw");


    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public double getFramerate() {
        return frameRate;
    }
}
