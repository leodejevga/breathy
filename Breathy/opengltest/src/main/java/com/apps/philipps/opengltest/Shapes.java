package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;

/**
 * Created by Jevgenij Huebert on 30.03.2017. Project Breathy
 */

public class Shapes {
    public static class Triangle extends GameObject3D.Shape {

        public Triangle(Context context) {
            this(context, new Vector(10, 0, 140),
                    3, 3, 3, R.drawable.streettexture,
                    // vertex
                    new Vector(0, 0.622008459f, 1.0f), new Vector(-0.5f, -0.311004243f, 1.0f), new Vector(0.5f, -0.311004243f, 1.0f),
                    // color
                    new Vector(1.0f, 1.0f, 1.0f, 1.0f), new Vector(1.0f, 1.0f, 1.0f, 1.0f), new Vector(1.0f, 1.0f, 1.0f, 1.0f),
                    // texture
                    new Vector(0f, 0f), new Vector(0f, 1f), new Vector(1f, 1f)


            );
        }

        public Triangle(Context context, int dimensions, Vector position,
                        int vertexCount, int colorCount, int textureCount, int textureID,
                        float... coords) {
            super(context, false, dimensions, position, vertexCount, colorCount, textureCount, textureID, coords);
        }

        public Triangle(Context context, Vector position,
                        int vertexCount, int colorCount, int textureCount, int textureID,
                        Vector... coords) {
            super(context, false, position, vertexCount, colorCount, textureCount, textureID, coords);
        }
    }

    public static class Square extends GameObject3D.Shape {
        public Square(Context context) {
            this(context, new Vector(100, 100, 100),
                    6, 6, 6, R.drawable.streettexture,
                    // vertex
                    new Vector(-0.5f, 0.5f, 0), new Vector(-0.5f, -0.5f, 0), new Vector(0.5f, -0.5f, 0),
                    new Vector(-0.5f, 0.5f, 0), new Vector(0.5f, -0.5f, 0), new Vector(0.5f, 0.5f, 0),
                    // color
                    new Vector(1.0f, 1.0f, 1.0f, 1.0f), new Vector(1.0f, 1.0f, 1.0f, 1.0f), new Vector(1.0f, 1.0f, 1.0f, 1.0f),
                    new Vector(1.0f, 1.0f, 1.0f, 1.0f), new Vector(1.0f, 1.0f, 1.0f, 1.0f), new Vector(1.0f, 1.0f, 1.0f, 1.0f),
                    // texture
                    new Vector(0.0f, 1.0f), new Vector(0.0f, 0.0f), new Vector(1.0f, 0.0f),
                    new Vector(0.0f, 1.0f), new Vector(1.0f, 0.0f), new Vector(1.0f, 1.0f)
            );
        }

        public Square(Context context, int dimensions, Vector position,
                      int vertexCount, int colorCount, int textureCount, int textureID,
                      float... coords) {
            super(context, false, dimensions, position, vertexCount, colorCount, textureCount, textureID, coords);
        }

        public Square(Context context, Vector position,
                      int vertexCount, int colorCount, int textureCount, int textureID,
                      Vector... coords) {
            super(context, false, position, vertexCount, colorCount, textureCount, textureID, coords);
        }
    }
}
