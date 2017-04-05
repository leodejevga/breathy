package com.apps.philipps.opengltest;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;

/**
 * Created by Jevgenij Huebert on 30.03.2017. Project Breathy
 */

public class Shapes {
    public static class Triangle extends GameObject3D.Shape{

        public Triangle(){
            this(new Vector(10, 0, 140), new Vector(0, 0, 0), new Vector(0, 0.622008459f, 0),
                    new Vector(-0.5f, -0.311004243f, 0), new Vector(0.5f, -0.311004243f, 0.0f));
        }

        public Triangle(Vector color, int dimensions, Vector position, float... coords) {
            super(color, dimensions, position, coords);
        }

        public Triangle(Vector color, Vector position, Vector... coords) {
            super(color, position, coords);
        }
    }
    public static class Square extends GameObject3D.Shape{
        public Square(){
            this(new Vector(100, 20, 0), new Vector(0, 0, 0), new Vector(-0.5f, 0.5f, 0),
                    new Vector(-0.5f, -0.5f, 0), new Vector(0.5f, -0.5f, 0.0f),
                    new Vector(-0.5f,  0.5f, 0.0f), new Vector(0.5f, -0.5f, 0.0f), new Vector(0.5f, 0.5f, 0.0f));
        }

        public Square(Vector color, int dimensions, Vector position, float... coords) {
            super(color, dimensions, position, coords);
        }

        public Square(Vector color, Vector position, Vector... coords) {
            super(color, position, coords);
        }
    }
}
