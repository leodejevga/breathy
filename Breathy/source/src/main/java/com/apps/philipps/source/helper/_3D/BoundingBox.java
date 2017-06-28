package com.apps.philipps.source.helper._3D;

import android.opengl.Matrix;

import com.apps.philipps.source.helper.Vector;

import java.util.ArrayList;


public class BoundingBox {

    private Vector[] points = new Vector[8];
    private float[] max_min_value;
    private float maxX;
    private float maxY;
    private float maxZ;
    private float minX;
    private float minY;
    private float minZ;
    Vector position = new Vector(0, 0, 0);
    ArrayList<Line> lines = new ArrayList<>();


    public BoundingBox(Vector[] vertex) {
        calculateMaxMinValue(vertex);
        this.max_min_value = new float[]{maxX, maxY, maxZ, minX, minY, minZ};
        rotate(new Vector(1, 0, 0, -90));
        generateLines();
    }

    public Vector getMax_min_value() {
        return new Vector(max_min_value);
    }

    private boolean isSetToOrigin() {
        return Math.abs(position.get(0)) < 0.0001f
                && Math.abs(position.get(1)) < 0.0001f
                && Math.abs(position.get(2)) < 0.0001f;
    }

    public void translate(Vector vector) {
        float[] temp = new float[16];
        Matrix.setIdentityM(temp, 0);
        Matrix.translateM(temp, 0, vector.get(0), vector.get(1), vector.get(2));
        multipleMatrix(temp);
        position.add(vector);
    }

    public void rotate(Vector vector) {
        float[] temp = new float[16];
        Matrix.setIdentityM(temp, 0);
        Matrix.rotateM(temp, 0, temp, 0, vector.get(3), vector.get(0), vector.get(1), vector.get(2));
        multipleMatrix(temp);
    }

    private void multipleMatrix(float[] transformMatrix) {
        for (int i = 0; i < points.length; i++) {
            float[] vector = new float[4];
            vector[0] = points[i].get(0);
            vector[1] = points[i].get(1);
            vector[2] = points[i].get(2);
            vector[3] = 1;
            Matrix.multiplyMV(vector, 0, transformMatrix, 0, vector, 0);
            points[i].set(new Vector(vector[0], vector[1], vector[2]));
        }
    }

    private void calculateMaxMinValue(Vector[] vectors) {
        this.maxX = -Float.MAX_VALUE;
        this.maxY = -Float.MAX_VALUE;
        this.maxZ = -Float.MAX_VALUE;
        this.minX = Float.MAX_VALUE;
        this.minY = Float.MAX_VALUE;
        this.minZ = Float.MAX_VALUE;
        Vector[] temp = new Vector[vectors.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = vectors[i].clone();
        }

        for (int i = 0; i < temp.length; i++) {
            Vector v = temp[i];
            if (v.get(0) > this.maxX)
                this.maxX = v.get(0);
            if (v.get(0) < this.minX)
                this.minX = v.get(0);

            if (v.get(1) > this.maxY)
                this.maxY = v.get(1);
            if (v.get(1) < this.minY)
                this.minY = v.get(1);

            if (v.get(2) > this.maxZ)
                this.maxZ = v.get(2);
            if (v.get(2) < this.minZ)
                this.minZ = v.get(2);
        }
        this.points[0] = new Vector(maxX, maxY, maxZ);
        this.points[1] = new Vector(minX, maxY, maxZ);
        this.points[2] = new Vector(minX, minY, maxZ);
        this.points[3] = new Vector(maxX, minY, maxZ);
        this.points[4] = new Vector(maxX, maxY, minZ);
        this.points[5] = new Vector(minX, maxY, minZ);
        this.points[6] = new Vector(minX, minY, minZ);
        this.points[7] = new Vector(maxX, minY, minZ);
    }

    public boolean collision(BoundingBox box) {

        return (minX <= box.maxX && maxX >= box.minX) &&
                (minY <= box.maxY && maxY >= box.minY) &&
                (minZ <= box.maxZ && maxZ >= box.minZ);
    }

    public void generateLines() {
        for (int i = 0; i < points.length - 1; i++)
            for (int j = i + 1; j < points.length; j++) {
                Line l = new Line();
                l.SetVerts(points[i], points[j]);
                lines.add(l);
            }
    }

    public void drawLines() {
        renewLinesPosition();
        for (Line line : lines) {
            line.draw();
        }
    }

    public void renewLinesPosition() {
        int counter = 0;
        for (int i = 0; i < points.length - 1; i++)
            for (int j = i + 1; j < points.length; j++) {
                lines.get(counter).SetVerts(points[i], points[j]);
                counter++;
            }
        calculateMaxMinValue(points);
    }
}

