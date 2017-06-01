package com.apps.philipps.source.helper._3D;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.apps.philipps.source.helper.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 30.03.2017. Project Breathy
 */

public class Camera3D {


    private Vector direction = new Vector(0,0,0);
    private Vector position = new Vector(0,0,-3);
    private Vector up = new Vector(0,1,0);
    private Vector rotation = new Vector(0,0,1,0);

    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    private static boolean init = false;

    public Camera3D(int width, int height, Vector... vectors) {
        if(vectors.length>0)
            direction = vectors[0];
        if(vectors.length>1)
            position = vectors[1];
        if(vectors.length>2)
            up = vectors[2];
        if(vectors.length>3)
            rotation = vectors[3];

        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 50000);
        updateMatrices();
    }

    public float[] getMVPMatrix() {
        return mMVPMatrix;
    }

    public void set(Vector... vectors){
        if(vectors.length>0)
            direction = vectors[0];
        if(vectors.length>1)
            position = vectors[1];
        if(vectors.length>2)
            up = vectors[2];
        if(vectors.length>3)
            rotation = vectors[3];
        updateMatrices();
    }
    public void move(Vector... vectors){
        if(vectors.length>0)
            direction.add(vectors[0]);
        if(vectors.length>1)
            position.add(vectors[1]);
        if(vectors.length>2)
            up.add(vectors[2]);
        if(vectors.length>3) {
            vectors[3].add(new Vector(0,0,0,rotation.get(3)));
            rotation.set(vectors[3]);
        }
        updateMatrices();
    }

    public void changeSurface(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    private void updateMatrices(){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, position.get(0), position.get(1), position.get(2),
                direction.get(0), direction.get(1), direction.get(2),
                up.get(0), up.get(1), up.get(2));

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setRotateM(mRotationMatrix, 0, rotation.get(3), rotation.get(0), rotation.get(1), rotation.get(2));

        Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);
    }

    public Vector getDirection() {
        return direction.clone();
    }

    public Vector getPosition() {
        return position.clone();
    }

    public Vector getUp() {
        return up.clone();
    }

    public Vector getRotation() {
        return rotation.clone();
    }
}
