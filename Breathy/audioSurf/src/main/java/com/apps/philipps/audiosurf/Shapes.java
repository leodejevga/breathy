package com.apps.philipps.audiosurf;

import android.content.Context;

import com.apps.philipps.source.helper._3D.GameObject3D;
import com.apps.philipps.source.helper.Vector;

/**
 * Created by Jevgenij Huebert on 30.03.2017. Project Breathy
 */

public class Shapes {
    public static class Triangle extends GameObject3D.Shape{

        public Triangle(Context context){
            this(context, new Vector(10,0,140), new Vector(0,0,0),
                    3, 3, 3, R.drawable.background,
                    new Vector(1,1,0), new Vector(), new Vector(2),
                    new Vector(1.0f,1.0f,1.0f,1.0f), new Vector(1.0f,1.0f,1.0f,1.0f), new Vector(1.0f,1.0f,1.0f,1.0f),
                    new Vector(0,0), new Vector(0,1), new Vector(1,1)
            );
        }

        public Triangle(Context context, Vector color, int dimensions, Vector position,
                        int vertexCount, int colorCount, int textureCount, int textureID,
                        float... coords) {
            super(context, color, dimensions, position, vertexCount,  colorCount,  textureCount,  textureID, coords);
        }

        public Triangle(Context context, Vector color, Vector position,
                        int vertexCount, int colorCount, int textureCount, int textureID,
                        Vector... coords) {
            super(context, color, position, vertexCount,  colorCount,  textureCount,  textureID, coords);
        }
    }
}
