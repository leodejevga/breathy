package com.apps.philipps.source.helper._3D;

import android.opengl.GLES20;

import com.apps.philipps.source.helper.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

/**
 * This class generates line with GL
 * */
public class Line {
    private int glProgram;
    private int positionHandle;
    private int colorHandle;
    private int mvpMatrixHandle;
    private DoubleBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static double lineCoords[] = {
            0.0, 0.0, 0.0,
            1.0, 0.0, 0.0
    };

    private final int vertexCount = lineCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.0f, 1.0f, 0.0f, 1.0f};

    public Line() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                lineCoords.length * 8);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asDoubleBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(lineCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = Helper_Utils.compileShader(GLES20.GL_VERTEX_SHADER, Helper_Utils.line_vertex_shader);
        int fragmentShader = Helper_Utils.compileShader(GLES20.GL_FRAGMENT_SHADER, Helper_Utils.line_fragment_shader);

        glProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(glProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(glProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(glProgram);                  // creates OpenGL ES program executables
    }

    public void setVertices(Vector point1, Vector point2) {
        lineCoords[0] = point1.get(0);
        lineCoords[1] = point1.get(1);
        lineCoords[2] = point1.get(2);
        lineCoords[3] = point2.get(0);
        lineCoords[4] = point2.get(1);
        lineCoords[5] = point2.get(2);

        vertexBuffer.put(lineCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    public void draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(glProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(glProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(glProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mvpMatrixHandle = GLES20.glGetUniformLocation(glProgram, "uMVPMatrix");
        Renderer3D.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, Renderer3D.camera3D.getMVPMatrix(), 0);
        Renderer3D.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}