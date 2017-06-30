package com.apps.philipps.source.helper._3D;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by qwert on 26/05/2017.
 */

public class Light {

    /**
     * Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     * we multiply this by our transformation matrices.
     */
    private float[] lightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

    /**
     * Used to hold the current position of the light in world space (after transformation via model matrix).
     */
    private float[] lightPosInWorldSpace = new float[4];

    /**
     * Used to hold the transformed position of the light in eye space (after transformation via modelview matrix)
     */
    private float[] lightPosInEyeSpace = new float[4];

    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private float[] lightModel_Matrix = new float[16];

    /**
     * This is a handle to our light point program.
     */
    private int pointProgramHandle;

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] view_Matrix = Camera3D.mViewMatrix;

    /**
     * Allocate storage for the final combined matrix. This will be passed into the shader program.
     */
    private float[] model_view_Matrix = new float[16];

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private float[] projection_Matrix = Camera3D.mProjectionMatrix;

    private float speed = 1f;
    private float angle = 0;
    private boolean increase = true;

    public Light() {
        final int pointVertexShaderHandle = Helper_Utils.compileShader(GLES20.GL_VERTEX_SHADER, Helper_Utils.point_vertex_shader);
        final int pointFragmentShaderHandle = Helper_Utils.compileShader(GLES20.GL_FRAGMENT_SHADER, Helper_Utils.point_fragment_shader);
        pointProgramHandle = Helper_Utils.createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                new String[]{"a_Position"});
    }

    private void lightMovementSimulation() {
        if (angle > 85)
            increase = false;
        if (angle < -85)
            increase = true;
        if (increase)
            angle += speed;
        else angle -= speed;
    }

    public void setUpLight() {
        lightMovementSimulation();
        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(lightModel_Matrix, 0);
        Matrix.rotateM(lightModel_Matrix, 0, angle, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(lightModel_Matrix, 0, 0.0f, 0.0f, -1f);

        Matrix.multiplyMV(lightPosInWorldSpace, 0, lightModel_Matrix, 0, lightPosInModelSpace, 0);
        Matrix.multiplyMV(lightPosInEyeSpace, 0, view_Matrix, 0, lightPosInWorldSpace, 0);
    }

    public void drawLight() {
        GLES20.glUseProgram(pointProgramHandle);
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(pointProgramHandle, "u_MVPMatrix");
        Renderer3D.checkGlError("glGetUniformLocation");
        final int pointPositionHandle = GLES20.glGetAttribLocation(pointProgramHandle, "a_Position");
        Renderer3D.checkGlError("glGetAttribLocation");

        GLES20.glVertexAttrib3f(pointPositionHandle, lightPosInModelSpace[0], lightPosInModelSpace[1], lightPosInModelSpace[2]);

        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        Matrix.multiplyMM(model_view_Matrix, 0, view_Matrix, 0, lightModel_Matrix, 0);
        Matrix.multiplyMM(model_view_Matrix, 0, projection_Matrix, 0, model_view_Matrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, model_view_Matrix, 0);

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

    public float[] getLightPosInEyeSpace() {
        return lightPosInEyeSpace;
    }

    public void setLightPosInEyeSpace(float[] lightPosInEyeSpace) {
        this.lightPosInEyeSpace = lightPosInEyeSpace;
    }
}
