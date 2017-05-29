package com.apps.philipps.source.helper._3D;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.apps.philipps.source.AppState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */

public abstract class Renderer3D implements GLSurfaceView.Renderer {

    private long start = System.currentTimeMillis();
    private int frameRate;
    private int frames;
    private long second;
    protected long deltaTime;

    public static Camera3D camera3D;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (camera3D == null){
            camera3D = new Camera3D(width, height);
            Light.init();
        }
        else camera3D.changeSurface(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        deltaTime = System.currentTimeMillis() - start;
        if (deltaTime <= (1000 / AppState.framelimit.getLimit()))
            try {
                int millis = (int) (1000 / AppState.framelimit.getLimit() - 1 - deltaTime);
                Thread.sleep(millis > 0 ? millis : 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        frames++;
        if(System.currentTimeMillis()-second>=1000){
            second = System.currentTimeMillis();
            frameRate = frames;
            frames = 0;
        }
        start = System.currentTimeMillis();


    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("Renderer 3D", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public double getFramerate() {
        return frameRate;
    }
}
