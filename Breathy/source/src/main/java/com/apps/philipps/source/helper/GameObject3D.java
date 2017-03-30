package com.apps.philipps.source.helper;

import android.icu.text.RelativeDateTimeFormatter;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import com.apps.philipps.source.interfaces.IGameObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Jevgenij Huebert on 23.03.2017. Project Breathy
 */
public class GameObject3D implements IGameObject {

    private Shape shape;
    private Vector rotation = new Vector();

    /**
     * Instantiates a new Game object 3 d.
     *
     * @param shape the shape
     */
    public GameObject3D(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void setPosition(Vector position) {
        shape.setPosition(position);
    }

    @Override
    public void setRotation(Vector rotation) {
        this.rotation = rotation;
    }

    @Override
    public Vector getPosition() {
        return shape.getPosition();
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public void move(Vector destination, int speed) {

    }

    @Override
    public void move(Vector destination) {

    }

    @Override
    public void move(int speed) {

    }

    @Override
    public void update(long deltaMilliseconds) {

        float[] temp = new float[16];
        float[] result = new float[16];
        Matrix.setRotateM(temp, 0, rotation.get(3), rotation.get(0), rotation.get(1), rotation.get(2));

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        if(Renderer3D.camera3D!=null) {
            Matrix.multiplyMM(result, 0, Renderer3D.camera3D.getcMVPMatrix(), 0, temp, 0);
            shape.draw(result);
        }
    }

    @Override
    public boolean intersect(IGameObject gameObject) {
        return false;
    }

    @Override
    public Vector getBoundaries() {
        return null;
    }

    /**
     * The type Shape.
     */
    public static class Shape {
        private String vertexShaderCode =
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                        "void main() {" +
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}";

        private String fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";

        private int program;
        private FloatBuffer vertexBuffer;
        private Vector position;
        private int dimensions = 0;
        /**
         * The Coords.
         */
        float[] coords;
        /**
         * The Color.
         */
        Vector color;

        /**
         * Instantiates a new Shape.
         *
         * @param color      the color
         * @param dimensions the dimensions
         * @param position   the position
         * @param coords     the coords
         */
        public Shape(Vector color, int dimensions, Vector position, float... coords) {
            this.dimensions = dimensions;
            this.color = color.clone().normCoords();
            int d = this.color.getDimensions();
            this.color = d==3?new Vector(this.color, 1):d==2?new Vector(this.color,0,1):d==1?new Vector(this.color,0,0,1):d==0?new Vector(0,0,0,1):this.color;
            this.position = position;
            this.coords = coords;

            ByteBuffer bb = ByteBuffer.allocateDirect(this.coords.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(coords);
            vertexBuffer.position(0);


            int vertexShader = Renderer3D.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShader = Renderer3D.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);

            // create empty OpenGL ES Program
            program = GLES20.glCreateProgram();

            // add the vertex shader to program
            GLES20.glAttachShader(program, vertexShader);

            // add the fragment shader to program
            GLES20.glAttachShader(program, fragmentShader);

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(program);

        }

        /**
         * Instantiates a new Shape.
         *
         * @param color    the color
         * @param position the position
         * @param coords   the coords
         */
        public Shape(Vector color, Vector position, @NonNull Vector... coords) {
            this(color, coords.length>0?coords[0].getDimensions():0, position, transformVectors(coords));
        }

        private static float[] transformVectors(@NonNull Vector... vectors){
            int dimensions = vectors.length>0?vectors[0].getDimensions():0;
            float[] result = new float[vectors.length * dimensions];
            for (int i = 0; i < vectors.length; i++) {
                float[] temp = vectors[i].get();
                System.arraycopy(temp, 0, result, i * dimensions, temp.length);
            }
            return result;
        }

        /**
         * Sets position.
         *
         * @param position the position
         */
        public void setPosition(Vector position) {
            this.position = position;
        }

        /**
         * Gets position.
         *
         * @return the position
         */
        public Vector getPosition() {
            return position;
        }

        /**
         * Sets vertex shader code.
         *
         * @param vertexShaderCode the vertex shader code
         */
        public void setVertexShaderCode(String vertexShaderCode) {
            setShaderCodes(vertexShaderCode, fragmentShaderCode);
        }

        /**
         * Sets fragment shader code.
         *
         * @param fragmentShaderCode the fragment shader code
         */
        public void setFragmentShaderCode(String fragmentShaderCode) {
            setShaderCodes(vertexShaderCode, fragmentShaderCode);
        }

        /**
         * Sets shader codes.
         *
         * @param vertexShaderCode   the vertex shader code
         * @param fragmentShaderCode the fragment shader code
         */
        public void setShaderCodes(String vertexShaderCode, String fragmentShaderCode) {
            this.vertexShaderCode = vertexShaderCode;
            this.fragmentShaderCode = fragmentShaderCode;

            int vertexShader = Renderer3D.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShader = Renderer3D.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);

            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);
        }

        private float[] mvpMatrix;

        /**
         * Draw.
         */
        public void draw() {
            GLES20.glUseProgram(program);
            int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
            GLES20.glEnableVertexAttribArray(positionHandle);

            GLES20.glVertexAttribPointer(positionHandle, dimensions,
                    GLES20.GL_FLOAT, false,
                    dimensions * 4, vertexBuffer);

            int colorHandle = GLES20.glGetUniformLocation(program, "vColor");
            GLES20.glUniform4fv(colorHandle, 1, color.get(), 0);

            // get handle to shape's transformation matrix
            int mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,  coords.length / dimensions);
            GLES20.glDisableVertexAttribArray(positionHandle);
        }

        /**
         * Draw.
         *
         * @param mvpMatrix the mvp matrix
         */
        public void draw(float[] mvpMatrix) {
            this.mvpMatrix = mvpMatrix;
            draw();
        }
    }
}
