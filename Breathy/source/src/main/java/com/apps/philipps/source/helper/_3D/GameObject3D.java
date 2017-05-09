package com.apps.philipps.source.helper._3D;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apps.philipps.source.SaveData;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.interfaces.IGameObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 23.03.2017. Project Breathy
 */
public class GameObject3D implements IGameObject {

    private Shape shape;
    private Vector rotation = new Vector(0,0,1);

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
            Matrix.multiplyMM(result, 0, Renderer3D.camera3D.getMVPMatrix(), 0, temp, 0);
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
            Renderer3D.checkGlError("glGetUniformLocation");

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
            Renderer3D.checkGlError("glUniformMatrix4fv");

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


//    std::vector< unsigned int > vertexIndices, uvIndices, normalIndices;
//    std::vector< glm::vec3 > temp_vertices;
//    std::vector< glm::vec2 > temp_uvs;
//    std::vector< glm::vec3 > temp_normals;

    public static Shape loadObject(String name){
        byte[] data = SaveData.readFile(Environment.getExternalStorageDirectory() + "/" + name);
        List<Vector> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        String line="";
        float max = 0;
        for(int i = 0;i<data.length; i++){
            char c = (char)data[i];
            if(c != '\n' && c != '\r')
                line += c;
            else if(line.length()!=0){
                if(line.startsWith("v ")){
                    String[] v = line.replaceAll(" +", " ").substring(2).split(" ");
                    if(v[0].length()==0 || v[1].length()==0 || v[2].length()==0)
                        Log.d("error", "vector " + v);
                    Vector vec = new Vector(Float.parseFloat(v[0]),
                            Float.parseFloat(v[1]),
                            Float.parseFloat(v[2]));
                    vertices.add(vec);
                    if(vec.get(0)>max || -vec.get(0)>max)
                        max = Math.abs(vec.get(0));
                    if(vec.get(1)>max || -vec.get(1)>max)
                        max = Math.abs(vec.get(1));
                    if(vec.get(2)>max || -vec.get(2)>max)
                        max = Math.abs(vec.get(2));
                }
                if(line.startsWith("f ")){
                    String[] v = line.replaceAll(" +", " ").substring(2).split("/\\d+/\\d+ *");
                    indices.add(Integer.parseInt(v[0])-1);
                    indices.add(Integer.parseInt(v[1])-1);
                    indices.add(Integer.parseInt(v[2])-1);
                }
                line = "";
            }

        }
        float[] coordinates = new float[indices.size()*3];
        for(int i=0; i<indices.size(); i++){
            Vector v = vertices.get(indices.get(i));
            coordinates[i*3] = v.get(0)/max;
            coordinates[i*3+1] = v.get(1)/max;
            coordinates[i*3+2] = v.get(2)/max;
        }

        return new Shape(new Vector(1,1,1,1), 3, new Vector(), coordinates);
    }
}
