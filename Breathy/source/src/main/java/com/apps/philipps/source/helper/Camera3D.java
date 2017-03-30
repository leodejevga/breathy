package com.apps.philipps.source.helper;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by Jevgenij Huebert on 30.03.2017. Project Breathy
 */

public class Camera3D {

    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    private static boolean init = false;

    public Camera3D(int width, int height) {
        this(width, height, new Vector(0, 0, -3),
                new Vector(0f, 0f, 0f), new Vector(0f, 1.0f, 0.0f));
    }

    public Camera3D(int width, int height, Vector position, Vector eye, Vector up) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        Matrix.setLookAtM(mViewMatrix, 0, eye.get(0), eye.get(1), eye.get(2),
                position.get(0), position.get(1), position.get(2),
                up.get(0), up.get(1), up.get(2));
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    public float[] getcMVPMatrix() {
        return mMVPMatrix;
    }

    public void changeSurface(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }
}
