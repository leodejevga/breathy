package com.apps.philipps.source.helper._3D;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.interfaces.IGameObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Jevgenij Huebert on 23.03.2017. Project Breathy
 */
public class GameObject3D implements IGameObject {

    private Shape shape;
    private Vector rotation = new Vector(0, 0, 0, 0);
    private float[] result = new float[16];
    private boolean isResultMatrixSet = false;

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

    /**
     * Gets rotation.
     */
    @Override
    public Vector getRotation() {
        return rotation;
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
    public void move(Vector destination, double speed) {

    }

    @Override
    public void rotate(Vector destination) {
        init();
        setRotation(destination);
        float[] temp = new float[16];
        Matrix.setRotateM(temp, 0, rotation.getF(3), rotation.getF(0), rotation.getF(1), rotation.getF(2));
        Matrix.multiplyMM(result, 0, result, 0, temp, 0);
        Matrix.multiplyMM(shape.model_Matrix, 0, shape.model_Matrix, 0, temp, 0);
    }

    @Override
    public void move(Vector destination) {
        init();
        shape.setPosition(getPosition().add(destination));
        float[] temp = new float[16];
        Matrix.setIdentityM(temp, 0);
        Matrix.translateM(temp, 0, getPosition().getF(0), getPosition().getF(1), getPosition().getF(2));
        Matrix.multiplyMM(result, 0, result, 0, temp, 0);
        Matrix.multiplyMM(shape.model_Matrix, 0, shape.model_Matrix, 0, temp, 0);
    }

    @Override
    public void move(double speed) {

    }

    @Override
    public void update(double deltaMilliseconds) {
        init();
        shape.draw(result);
        isResultMatrixSet = false;
    }

    @Override
    public boolean intersect(IGameObject gameObject) {
        return false;
    }

    @Override
    public Vector getBoundaries() {
        return this.shape.boundingBox.getMax_min_value();
    }

    public BoundingBox getBoundingBox() {
        return this.shape.boundingBox;
    }


    private void init() {
        if (Renderer3D.camera3D != null) {
            if (!isResultMatrixSet) {
                System.arraycopy(Renderer3D.camera3D.getMVPMatrix(), 0, result, 0, Renderer3D.camera3D.getMVPMatrix().length);
                Matrix.setIdentityM(shape.model_Matrix, 0);
                isResultMatrixSet = true;
            }
        }
    }

    /**
     * The type Shape.
     */
    public static class Shape {
        private Vector position;
        private int dimensions = 0;
        BoundingBox boundingBox;
        /**
         * The Coords.
         */
        float[] coords;

        /**
         * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
         * it positions things relative to our eye.
         */
        private float[] view_Matrix = Camera3D.mViewMatrix;
        /**
         * Store the projection matrix. This is used to project the scene onto a 2D viewport.
         */

        /**
         * Allocate storage for the final combined matrix. This will be passed into the shader program.
         */
        private float[] model_view_Matrix = new float[16];

        private float[] model_Matrix = new float[16];

        /**
         * Store our model data in a float buffer.
         */
        private FloatBuffer positions_Buffer;
        private FloatBuffer colors_Buffer;
        private FloatBuffer normals_Buffer;
        private FloatBuffer textureCoordinates_Buffer;

        /**
         * This will be used to pass in the transformation matrix.
         */
        private int Model_View_Projection_MatrixHandle;

        /**
         * This will be used to pass in the modelview matrix.
         */
        private int Model_View_MatrixHandle;

        /**
         * This will be used to pass in the light position.
         */
        private int mLightPosHandle;

        /**
         * This will be used to pass in the light positions
         */
        private int uPointLightPositionsLocation;

        /**
         * This will be used to pass in the light colors.
         */
        private int uPointLightColorsLocation;

        /**
         * This will be used to pass in the texture.
         */
        private int textureUniformHandle;

        /**
         * This will be used to pass in model position information.
         */
        private int positionHandle;

        /**
         * This will be used to pass in model color information.
         */
        private int colorHandle;

        /**
         * This will be used to pass in model normal information.
         */
        private int normalHandle;

        /**
         * This will be used to pass in model texture coordinate information.
         */
        private int TextureCoordinateHandle;

        /**
         * How many bytes per float.
         */
        private final int mBytesPerFloat = 4;

        /**
         * Size of the position data in elements.
         */
        private final int positionDataSize = 3;

        /**
         * Size of the color data in elements.
         */
        private final int colorDataSize = 4;

        /**
         * Size of the normal data in elements.
         */
        private final int normalDataSize = 3;

        /**
         * Size of the texture coordinate data in elements.
         */
        private final int TextureCoordinateDataSize = 2;

        /**
         * This is a handle to our cube shading program.
         */
        private int programHandle;

        /**
         * This is a handle to our texture data.
         */
        private int textureDataHandle;

        float[] normalData;

        float[] textureCoordinateData;
        float[] colorData;
        int vertexShaderHandle;
        int fragmentShaderHandle;

        /**
         * Instantiates a new Shape.
         *
         * @param dimensions the dimensions
         * @param position   the position
         * @param coords     the coords
         */

        private boolean showNormalColor = false;

        private boolean waveFrontObject = false;

        public Shape(Context context, boolean isWaveFrontObj, int dimensions, Vector position,
                     int vertexCount, int colorCount, int textureCount, int textureID,
                     float... coords) {
            this.dimensions = dimensions;
            this.position = position;
            this.waveFrontObject = isWaveFrontObj;
            this.coords = new float[vertexCount * dimensions];
            System.arraycopy(coords, 0, this.coords, 0, vertexCount * dimensions);
            this.colorData = new float[colorCount * 4];
            System.arraycopy(coords, vertexCount * dimensions, this.colorData, 0, colorCount * 4);
            this.textureCoordinateData = new float[textureCount * 2];
            System.arraycopy(coords, vertexCount * dimensions + colorCount * 4, this.textureCoordinateData, 0, textureCount * 2);

            // normal vectors need to be calculated if and only if object is NOT a wavefront Object
            Vector[] temp = transformArrays(dimensions, this.coords);
            normalData = new float[this.coords.length];
            if (isWaveFrontObj) {
                System.arraycopy(coords, vertexCount * dimensions + colorCount * 4 + textureCount * 2, this.normalData, 0, vertexCount * dimensions);
            } else {
                /*calculate normal matrix from model view matrix*/
                calculateNormals(temp);
            }
            calculateBounding(temp);

            normals_Buffer = ByteBuffer.allocateDirect(normalData.length * mBytesPerFloat)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            normals_Buffer.put(normalData).position(0);

            positions_Buffer = ByteBuffer.allocateDirect(this.coords.length * mBytesPerFloat)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            positions_Buffer.put(this.coords).position(0);

            if (showNormalColor) {
                copyNormalToColor();
            }
            colors_Buffer = ByteBuffer.allocateDirect(colorData.length * mBytesPerFloat)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            colors_Buffer.put(colorData).position(0);

            textureCoordinates_Buffer = ByteBuffer.allocateDirect(textureCoordinateData.length * mBytesPerFloat)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            textureCoordinates_Buffer.put(textureCoordinateData).position(0);

            vertexShaderHandle = Helper_Utils.compileShader(GLES20.GL_VERTEX_SHADER, Helper_Utils.vertex_texture_shader);
            fragmentShaderHandle = Helper_Utils.compileShader(GLES20.GL_FRAGMENT_SHADER, Helper_Utils.fragment_texture_shader);

            programHandle = Helper_Utils.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                    new String[]{"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});
            // Load the texture
            textureDataHandle = TextureHelper.loadTexture(context, textureID);
        }

        private void copyNormalToColor() {
            for (int i = 0; i < this.colorData.length / 4; i++) {
                this.colorData[i * 4] = Math.abs(this.normalData[i * 3]);
                this.colorData[i * 4 + 1] = Math.abs(this.normalData[i * 3 + 1]);
                this.colorData[i * 4 + 2] = Math.abs(this.normalData[i * 3 + 2]);
            }
        }

        /**
         * Instantiates a new Shape.
         *
         * @param position the position
         * @param coords   the coords
         */
        public Shape(Context context, boolean isWaveFrontObj, Vector position,
                     int vertexCount, int colorCount, int textureCount, int textureID,
                     @NonNull Vector... coords) {
            this(context, isWaveFrontObj, coords.length > 0 ? coords[0].getDimensions() : 0, position,
                    vertexCount, colorCount, textureCount, textureID,
                    transformVectors(coords));
        }

        private static float[] transformVectors(@NonNull Vector... vectors) {
            ArrayList<Float> list = new ArrayList<>();
            for (int i = 0; i < vectors.length; i++) {
                float[] temp = vectors[i].getF();
                for (int j = 0; j < temp.length; j++)
                    list.add(temp[j]);
            }
            float[] result = new float[list.size()];
            for (int i = 0; i < list.size(); i++)
                result[i] = list.get(i);
            return result;
        }

        private static Vector[] transformArrays(int dimensions, @NonNull float[] coords) {
            if (coords.length % dimensions == 0) {
                Vector[] result = new Vector[coords.length / dimensions];
                for (int i = 0; i < result.length; i++) {
                    float[] temp = new float[dimensions];
                    for (int j = 0; j < dimensions; j++) {
                        temp[j] = coords[i * dimensions + j];
                    }
                    result[i] = new Vector(temp);
                }
                return result;
            } else {
                return null;
            }
        }

        /**
         * Sets position.
         *
         * @param position the position
         */
        public void setPosition(Vector position) {
            this.position = position;
            //Current position of Bounding Box
            if (boundingBox.isNeedToCalculateBB()) {
                Vector currentPos = boundingBox.position;
                Vector inversePos = new Vector(-currentPos.getF(0), -currentPos.getF(1), -currentPos.getF(2));
                this.boundingBox.translate(inversePos.add(position));
            }
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

        private float[] model_view_projection_Matrix;


        /**
         * Draw.
         */
        public void draw() {
            // Set our per-vertex lighting program.
            GLES20.glUseProgram(programHandle);

            // Set program handles for cube drawing.
            Model_View_Projection_MatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
            Renderer3D.checkGlError("glGetUniformLocation");
            Model_View_MatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVMatrix");
            Renderer3D.checkGlError("glGetUniformLocation");

            textureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
            Renderer3D.checkGlError("glGetUniformLocation");
            uPointLightPositionsLocation = GLES20.glGetUniformLocation(programHandle, "u_PointLightPositions");
            Renderer3D.checkGlError("glGetUniformLocation");
            uPointLightColorsLocation = GLES20.glGetUniformLocation(programHandle, "u_PointLightColors");
            Renderer3D.checkGlError("glGetUniformLocation");

            positionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
            colorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
            normalHandle = GLES20.glGetAttribLocation(programHandle, "a_Normal");
            TextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);

            // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
            GLES20.glUniform1i(textureUniformHandle, 0);

            drawTrianglePlane();
        }

        private void drawTrianglePlane() {
            // Pass in the position information
            positions_Buffer.position(0);
            GLES20.glVertexAttribPointer(positionHandle, positionDataSize, GLES20.GL_FLOAT, false,
                    0, positions_Buffer);
            Renderer3D.checkGlError("glVertexAttribPointer");
            GLES20.glEnableVertexAttribArray(positionHandle);

            // Pass in the color information
            colors_Buffer.position(0);
            GLES20.glVertexAttribPointer(colorHandle, colorDataSize, GLES20.GL_FLOAT, false,
                    0, colors_Buffer);
            Renderer3D.checkGlError("glVertexAttribPointer");

            GLES20.glEnableVertexAttribArray(colorHandle);

            /*calculate normal vectors from model view matrix*/
            Matrix.multiplyMM(model_view_Matrix, 0, view_Matrix, 0, model_Matrix, 0);

            // Pass in the normal information
            normals_Buffer.position(0);
            GLES20.glVertexAttribPointer(normalHandle, normalDataSize, GLES20.GL_FLOAT, false,
                    0, normals_Buffer);
            Renderer3D.checkGlError("glVertexAttribPointer");
            GLES20.glEnableVertexAttribArray(normalHandle);
            Renderer3D.checkGlError("glEnableVertexAttribArray");
            // Pass in the texture coordinate information
            textureCoordinates_Buffer.position(0);
            GLES20.glVertexAttribPointer(TextureCoordinateHandle, TextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                    0, textureCoordinates_Buffer);
            Renderer3D.checkGlError("glVertexAttribPointer");
            GLES20.glEnableVertexAttribArray(TextureCoordinateHandle);
            Renderer3D.checkGlError("glEnableVertexAttribArray");
            // Pass in the modelview matrix.
            GLES20.glUniformMatrix4fv(Model_View_MatrixHandle, 1, false, model_view_Matrix, 0);
            Renderer3D.checkGlError("glUniformMatrix4fv");
            // Pass in the combined matrix.
            GLES20.glUniformMatrix4fv(Model_View_Projection_MatrixHandle, 1, false, model_view_projection_Matrix, 0);
            Renderer3D.checkGlError("glUniformMatrix4fv");
            GLES20.glUniform3fv(uPointLightColorsLocation, Light.pointLightColors.length / 3, Light.pointLightColors, 0);
            Renderer3D.checkGlError("glUniformMatrix3fv");
            GLES20.glUniform4fv(uPointLightPositionsLocation, Light.lightPosInEyeSpace.length / 4, Light.lightPosInEyeSpace, 0);
            Renderer3D.checkGlError("glUniformMatrix4fv");
            // Pass in the light position in eye space.
            // Draw a fragment
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, coords.length / dimensions);
        }

        /**
         * Draw.
         *
         * @param mvpMatrix the mvp matrix
         */
        public void draw(float[] mvpMatrix) {
            this.model_view_projection_Matrix = mvpMatrix;
            draw();
        }

        private void calculateBounding(Vector[] vertex) {
            this.boundingBox = new BoundingBox(vertex);
        }

        private void calculateNormals(Vector[] vertex) {
            int numberOfPlane = vertex.length / 3;
            for (int i = 0; i < numberOfPlane; i++) {
                Vector v1 = vertex[i * 3];
                Vector v2 = vertex[i * 3 + 1];
                Vector v3 = vertex[i * 3 + 2];
                Vector u = Vector.sub(v1, v3);
                Vector v = Vector.sub(v1, v2);

                Vector normal = Vector.cross(u, v);
                if (normal == null) {
                    normal = new Vector(0, 0, 0);
                }
                for (int j = 0; j < 3; j++) {
                    int index = (i * 9) + (j * 3);
                    normalData[index] = waveFrontObject ? -normal.getF(0) : normal.getF(0);
                    normalData[index + 1] = waveFrontObject ? -normal.getF(1) : normal.getF(1);
                    normalData[index + 2] = waveFrontObject ? -normal.getF(2) : normal.getF(2);
                }
            }
        }

        public boolean isWaveFrontObject() {
            return waveFrontObject;
        }

        public void setWaveFrontObject(boolean waveFrontObject) {
            this.waveFrontObject = waveFrontObject;
        }
    }

    public static Shape loadObject(Context context, int objID, int textureID) {
        ObjLoader objLoader = new ObjLoader(context, objID);

        float[] colorArray = generateColorArray(objLoader.positions.length * 4 / 3);
        float[] coords = concatArrays(objLoader.positions, concatArrays(colorArray, concatArrays(objLoader.textureCoordinates, objLoader.normals)));

        Shape waveFrontObj = new Shape(context, true, 3, new Vector(), objLoader.positions.length / 3, colorArray.length / 4,
                objLoader.textureCoordinates.length / 2, textureID, coords);
        waveFrontObj.setWaveFrontObject(true);
        return waveFrontObj;
    }

    private static float[] generateColorArray(int length) {
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = 1.0f;
        }
        return array;
    }

    private static float[] concatArrays(float[] array1, float[] array2) {
        float[] array1and2 = new float[array1.length + array2.length];
        System.arraycopy(array1, 0, array1and2, 0, array1.length);
        System.arraycopy(array2, 0, array1and2, array1.length, array2.length);
        return array1and2;
    }
}
