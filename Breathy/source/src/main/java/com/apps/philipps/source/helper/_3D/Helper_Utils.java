package com.apps.philipps.source.helper._3D;

import android.opengl.GLES20;
import android.util.Log;


public class Helper_Utils {
    public static String number_of_light = "3";

    public static String vertex_texture_shader =
                    "uniform mat4 u_MVPMatrix;" +
                    "uniform mat4 u_MVMatrix;" +
                    "uniform vec4 u_PointLightPositions["+ number_of_light +"];" +
                    "uniform vec3 u_PointLightColors["+ number_of_light +"];" +

                    "attribute vec4 a_Position;" +  // Per-vertex position information we will pass in.
                    "attribute vec4 a_Color;" +      // Per-vertex color information we will pass in.
                    "attribute vec3 a_Normal;" +      // Per-vertex normal information we will pass in.
                    "attribute vec2 a_TexCoordinate;" +// Per-vertex texture coordinate information we will pass in.

                    "varying vec3 v_Position;" +     // This will be passed into the fragment shader.
                    "varying vec4 v_Color;" +       // This will be passed into the fragment shader.
                    "varying vec3 v_Normal;" +   // This will be passed into the fragment shader.
                    "varying vec2 v_TexCoordinate;" +// This will be passed into the fragment shader.
                    "varying vec3 lighting;" +
                    "vec3 getPointLighting();" +
                    // The entry point for our vertex shader.
                    "void main()" +
                    "{" +
                    // Transform the vertex into eye space.
                    "v_Position = vec3(u_MVMatrix * a_Position);" +
                    // Pass through the color.
                    "v_Color = a_Color;" +
                    // Pass through the texture coordinate.
                    "v_TexCoordinate = a_TexCoordinate;" +
                    // Transform the normal's orientation into eye space.
                    "v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));" +
                    // gl_Position is a special variable used to store the final position.
                    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                    "gl_Position = u_MVPMatrix * a_Position;" +
                    "lighting = getPointLighting();"+
                    "}" +

                    "vec3 getPointLighting()" +
                    "{" +
                        "vec3 lightingSum = vec3(0.0);" +
                        "for (int i = 0; i < "+ number_of_light +"; i++) {" +
                            "float specular = 0.0;"+
                            "vec3 toPointLight = vec3(u_PointLightPositions[i]) - vec3(v_Position);" +
                            "float distance = length(toPointLight);" +
                            "toPointLight = normalize(toPointLight);" +
                            "float lambertian =  max(dot(v_Normal, toPointLight), 0.0);"+
                            "float diffuse = lambertian * (1.0 / (1.0 + (0.25 * distance * distance)));" +
                            "if(lambertian > 0.0) {"+
                                "vec3 reflectDir = reflect(-toPointLight, v_Normal);"+
                                "vec3 viewDir = normalize(-v_Position);"+
                                "float specAngle = max(dot(reflectDir, viewDir), 0.0);" +
                                "specular = pow(specAngle, 4.0);"+
                            "}"+

                            "lightingSum += ((lambertian * diffuse  * specular) * u_PointLightColors[i]);" +
                        "}" +
                        "return lightingSum;" +
                    "}";

    public static String fragment_texture_shader =
                    "precision mediump float;" +    // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    "uniform sampler2D u_Texture;" + // The input texture.

                    "varying vec3 v_Position;" +      // Interpolated position for this fragment.
                    "varying vec4 v_Color;" +    // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    "varying vec3 v_Normal;" +   // Interpolated normal for this fragment.
                    "varying vec2 v_TexCoordinate;" + // Interpolated texture coordinate per fragment.
                    "varying vec3 lighting;"+

                    // The entry point for our fragment shader.
                    "void main()" +
                    "{" +
                    // Multiply the color by the diffuse illumination level and texture value to get final output color.
                    "gl_FragColor = ((v_Color + vec4(lighting,1.0)) * texture2D(u_Texture, v_TexCoordinate));" +
                    //"gl_FragColor = (vec4(v_Normal, 0.0) * diffuse * texture2D(u_Texture, v_TexCoordinate));"+
                    // "gl_FragColor = vColor * texture2D(u_Texture, v_TexCoordinate);"+
                    "}";

    public static String point_vertex_shader =
                    "uniform mat4 u_MVPMatrix;" +
                    "attribute vec4 a_Position;" +

                    "void main()" +
                    "{" +
                    "gl_Position = u_MVPMatrix * a_Position;" +
                    "gl_PointSize = 10.0;" +
                    "}";

    public static String point_fragment_shader =
                    "precision mediump float;" +

                    "void main()" +
                    "{" +
                    "gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
                    "}";

    public static String line_vertex_shader =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
                    "uniform mat4 uMVPMatrix;" +

                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public static String line_fragment_shader =
                    "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    /**
     * Helper function to compile a shader.
     *
     * @param shaderType   The shader type.
     * @param shaderSource The shader source code.
     * @return An OpenGL handle to the shader.
     */
    public static int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.e("Helper_CompileShader", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    /**
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes           Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     */
    public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Log.e("Helper_CompileShader", "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
}

