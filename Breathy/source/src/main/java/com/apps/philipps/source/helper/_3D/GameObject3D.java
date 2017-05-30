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
    private Vector rotation = new Vector(0, 0, 1);
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
    public void move(Vector destination, int speed) {

    }

    @Override
    public void rotate(Vector destination) {
        init();
        setRotation(getRotation().add(destination));
        float[] temp = new float[16];
        Matrix.setRotateM(temp, 0, rotation.get(3), rotation.get(0), rotation.get(1), rotation.get(2));
        Matrix.multiplyMM(result, 0, result, 0, temp, 0);
        Matrix.multiplyMM(shape.model_Matrix, 0, shape.model_Matrix, 0, temp, 0);
        isResultMatrixSet = true;
    }

    @Override
    public void move(Vector destination) {
        init();
        setPosition(getPosition().add(destination));
        float[] temp = new float[16];
        Matrix.setIdentityM(temp, 0);
        Matrix.translateM(temp, 0, getPosition().get(0), getPosition().get(1), getPosition().get(2));
        Matrix.multiplyMM(result, 0, result, 0, temp, 0);
        Matrix.multiplyMM(shape.model_Matrix, 0, shape.model_Matrix, 0, temp, 0);
        isResultMatrixSet = true;
    }

    @Override
    public void move(int speed) {

    }

    @Override
    public void update(long deltaMilliseconds) {
        init();
        isResultMatrixSet = false;
        shape.draw(result);
    }

    @Override
    public boolean intersect(IGameObject gameObject) {
        return false;
    }

    @Override
    public Vector getBoundaries() {
        return null;
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
        /**
         * The Coords.
         */
        float[] coords;
        /**
         * The Color.
         */
        Vector color;
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
         * @param color      the color
         * @param dimensions the dimensions
         * @param position   the position
         * @param coords     the coords
         */
        public Shape(Context context, Vector color, int dimensions, Vector position,
                     int vertexCount, int colorCount, int textureCount, int textureID,
                     float... coords) {
            this.dimensions = dimensions;
            this.color = color.clone().normCoords();
            int d = this.color.getDimensions();
            this.color = d == 3 ? new Vector(this.color, 1) : d == 2 ? new Vector(this.color, 0, 1) : d == 1 ? new Vector(this.color, 0, 0, 1) : d == 0 ? new Vector(0, 0, 0, 1) : this.color;
            this.position = position;
            this.coords = new float[vertexCount * dimensions];
            System.arraycopy(coords, 0, this.coords, 0, vertexCount * dimensions);
            this.colorData = new float[colorCount * 4];
            System.arraycopy(coords, vertexCount * dimensions, this.colorData, 0, colorCount * 4);
            this.textureCoordinateData = new float[textureCount * 2];
            System.arraycopy(coords, vertexCount * dimensions + colorCount * 4, this.textureCoordinateData, 0, textureCount * 2);

            /*calculate normal matrix from model view matrix*/
            calculateNormalVertex();
            normals_Buffer = ByteBuffer.allocateDirect(normalData.length * mBytesPerFloat)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            normals_Buffer.put(normalData).position(0);

            positions_Buffer = ByteBuffer.allocateDirect(this.coords.length * mBytesPerFloat)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            positions_Buffer.put(this.coords).position(0);

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

        /**
         * Instantiates a new Shape.
         *
         * @param color    the color
         * @param position the position
         * @param coords   the coords
         */
        public Shape(Context context, Vector color, Vector position,
                     int vertexCount, int colorCount, int textureCount, int textureID,
                     @NonNull Vector... coords) {
            this(context, color, coords.length > 0 ? coords[0].getDimensions() : 0, position,
                    vertexCount, colorCount, textureCount, textureID,
                    transformVectors(coords));
        }

        private static float[] transformVectors(@NonNull Vector... vectors) {
            ArrayList<Float> list = new ArrayList<>();
            for (int i = 0; i < vectors.length; i++) {
                float[] temp = vectors[i].get();
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
            mLightPosHandle = GLES20.glGetUniformLocation(programHandle, "u_LightPos");
            Renderer3D.checkGlError("glGetUniformLocation");
            textureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
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

            GLES20.glEnableVertexAttribArray(positionHandle);

            // Pass in the color information
            colors_Buffer.position(0);
            GLES20.glVertexAttribPointer(colorHandle, colorDataSize, GLES20.GL_FLOAT, false,
                    0, colors_Buffer);

            GLES20.glEnableVertexAttribArray(colorHandle);

            /*calculate normal vectors from model view matrix*/
            Matrix.multiplyMM(model_view_Matrix, 0, view_Matrix, 0, model_Matrix, 0);

            // Pass in the normal information
            normals_Buffer.position(0);
            GLES20.glVertexAttribPointer(normalHandle, normalDataSize, GLES20.GL_FLOAT, false,
                    0, normals_Buffer);

            GLES20.glEnableVertexAttribArray(normalHandle);

            // Pass in the texture coordinate information
            textureCoordinates_Buffer.position(0);
            GLES20.glVertexAttribPointer(TextureCoordinateHandle, TextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                    0, textureCoordinates_Buffer);

            GLES20.glEnableVertexAttribArray(TextureCoordinateHandle);

            // Pass in the modelview matrix.
            GLES20.glUniformMatrix4fv(Model_View_MatrixHandle, 1, false, model_view_Matrix, 0);

            // Pass in the combined matrix.
            GLES20.glUniformMatrix4fv(Model_View_Projection_MatrixHandle, 1, false, model_view_projection_Matrix, 0);
            Renderer3D.checkGlError("glUniformMatrix4fv");

            // Pass in the light position in eye space.
            GLES20.glUniform3f(mLightPosHandle, Renderer3D.light.getLightPosInEyeSpace()[0],
                    Renderer3D.light.getLightPosInEyeSpace()[1], Renderer3D.light.getLightPosInEyeSpace()[2]);
            Renderer3D.checkGlError("glUniform3f");
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

        private void calculateNormalVertex() {
            normalData = new float[coords.length];
            Vector[] temp = transformArrays(dimensions, coords);
            int numberOfPlane = temp.length / 3;
            for (int i = 0; i < numberOfPlane; i++) {
                Vector v1 = temp[i * 3];
                Vector v2 = temp[i * 3 + 1];
                Vector v3 = temp[i * 3 + 2];
                Vector u = Vector.sub(v1, v3);
                Vector v = Vector.sub(v1, v2);

                Vector normal = Vector.cross(u, v);
                for (int j = 0; j < 3; j++) {
                    int index = (i * 9) + (j * 3);
                    normalData[index] = normal.get(0);
                    normalData[index + 1] = normal.get(1);
                    normalData[index + 2] = normal.get(2);

                }
            }
        }
    }


//    std::vector< unsigned int > vertexIndices, uvIndices, normalIndices;
//    std::vector< glm::vec3 > temp_vertices;
//    std::vector< glm::vec2 > temp_uvs;
//    std::vector< glm::vec3 > temp_normals;

   /* public static Shape loadObject(Context context, String name ) {
        byte[] data = SaveData.readFile(Environment.getExternalStorageDirectory() + "/" + name);
        List<Vector> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        String line = "";
        float max = 0;
        for (int i = 0; i < data.length; i++) {
            char c = (char) data[i];
            if (c != '\n' && c != '\r')
                line += c;
            else if (line.length() != 0) {
                if (line.startsWith("v ")) {
                    String[] v = line.replaceAll(" +", " ").substring(2).split(" ");
                    if (v[0].length() == 0 || v[1].length() == 0 || v[2].length() == 0)
                        Log.d("error", "vector " + v);
                    Vector vec = new Vector(Float.parseFloat(v[0]),
                            Float.parseFloat(v[1]),
                            Float.parseFloat(v[2]));
                    vertices.add(vec);
                    if (vec.get(0) > max || -vec.get(0) > max)
                        max = Math.abs(vec.get(0));
                    if (vec.get(1) > max || -vec.get(1) > max)
                        max = Math.abs(vec.get(1));
                    if (vec.get(2) > max || -vec.get(2) > max)
                        max = Math.abs(vec.get(2));
                }
                if (line.startsWith("f ")) {
                    String[] v = line.replaceAll(" +", " ").substring(2).split("/\\d+/\\d+ *");
                    indices.add(Integer.parseInt(v[0]) - 1);
                    indices.add(Integer.parseInt(v[1]) - 1);
                    indices.add(Integer.parseInt(v[2]) - 1);
                }
                line = "";
            }

        }
        float[] coordinates = new float[indices.size() * 3];
        for (int i = 0; i < indices.size(); i++) {
            Vector v = vertices.get(indices.get(i));
            coordinates[i * 3] = v.get(0) / max;
            coordinates[i * 3 + 1] = v.get(1) / max;
            coordinates[i * 3 + 2] = v.get(2) / max;
        }

        return new Shape(context, new Vector(1, 1, 1, 1), 3, new Vector(), coordinates);
    }*/
}
