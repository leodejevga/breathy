package com.apps.philipps.audiosurf;

import com.apps.philipps.source.helper.GameObject3D;
import com.apps.philipps.source.helper.Vector;

/**
 * Created by Jevgenij Huebert on 30.03.2017. Project Breathy
 */

public class Shapes {
    public static class Triangle extends GameObject3D.Shape{

        public Triangle(){
            this(new Vector(100,0,100), new Vector(0,0,0), new Vector(100,100,0), new Vector(), new Vector(2));
        }

        public Triangle(Vector color, int dimensions, Vector position, float... coords) {
            super(color, dimensions, position, coords);
        }

        public Triangle(Vector color, Vector position, Vector... coords) {
            super(color, position, coords);
        }
    }
}
