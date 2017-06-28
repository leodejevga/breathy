package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;


public class Shapes {
    public static class Square extends GameObject3D.Shape {
        public static final float  size = 0.7f;
        public Square(Context context) {

            this(context, new Vector(100, 100, 100),
                    6, 6, 6, R.drawable.streettexture,
                    // vertex
                    new Vector(-size, size, 0), new Vector(-size, -size, 0), new Vector(size, -size, 0),
                    new Vector(-size, size, 0), new Vector(size, -size, 0), new Vector(size, size, 0),
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
