package com.apps.philipps.source.helper;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 17.03.2017. Project Breathy
 */
public class Vector implements Comparable<Vector>, Cloneable {

    private List<Float> coordinates;

    public Vector(Vector v, float... coordinates) {
        this.coordinates = new ArrayList<>();
        for (float coordinate : v.get())
            this.coordinates.add(coordinate);

        for (float coordinate : coordinates)
            this.coordinates.add(coordinate);
    }

    /**
     * Instantiates a new Vector.
     *
     * @param coordinates the coordinates
     */
    public Vector(float... coordinates) {
        this.coordinates = new ArrayList<>();
        for (float coordinate : coordinates)
            this.coordinates.add(coordinate);
    }

    public int getDimensions() {
        return coordinates.size();
    }

    /**
     * Add position.
     *
     * @param position the position
     * @return the position
     */
    public Vector add(Vector position) {
        for (int i = 0; i < getDimensions() || i < position.getDimensions(); i++)
            set(i, get(i) + position.get(i));
        return this;
    }

    /**
     * Sub position.
     *
     * @param position the position
     * @return the position
     */
    public Vector sub(Vector position) {
        for (int i = 0; i < getDimensions() || i < position.getDimensions(); i++)
            set(i, get(i) - position.get(i));
        return this;
    }

    /**
     * Mult position.
     *
     * @param multiplier the multiplier
     * @return the position
     */
    public Vector mult(float multiplier) {
        for (int i = 0; i < getDimensions(); i++)
            set(i, get(i) * multiplier);
        return this;
    }

    /**
     * Mult position.
     *
     * @param start      the start
     * @param end        the end
     * @param multiplier the multiplier
     * @return the position
     */
    public Vector mult(int start, int end, float multiplier) {
        for (int i = start; i < getDimensions() && i <= end; i++)
            set(i, get(i) * multiplier);
        return this;
    }

    /**
     * Mult position.
     *
     * @param coordinate the coordinate
     * @param multiplier the multiplier
     * @return the position
     */
    public Vector mult(int coordinate, float multiplier) {
        set(coordinate, get(coordinate) * multiplier);
        return this;
    }

    /**
     * Divide position.
     *
     * @param divisor the divisor
     * @return the position
     */
    public Vector divide(float divisor) {
        if (divisor == 0)
            return null;
        for (int i = 0; i < getDimensions(); i++)
            set(i, get(i) / divisor);
        return this;
    }

    public Vector norm() {
        return divide(getDistance());
    }

    public Vector normCoords() {
        float max = 0;
        for (float c : coordinates)
            max = c > max ? c : max;
        return divide(max == 0 ? 1 : max);
    }

    /**
     * Get float.
     *
     * @param coordinate the coordinate
     * @return the float
     */
    public float get(int coordinate) {
        if (coordinate < coordinates.size())
            return coordinates.get(coordinate);
        else return 0;
    }

    public void set(Vector v) {
        for (int i = 0; i < v.getDimensions(); i++) {
            if (i < coordinates.size())
                coordinates.set(i, v.get(i));
            else
                coordinates.add(v.get(i));
        }
    }

    public void set(int index, float data) {
        if (getDimensions() > index) {
            coordinates.set(index, data);
            return;
        }
        for (int i = getDimensions(); i < index; i++)
            coordinates.add(0f);
        coordinates.add(data);
    }

    public void set(float... floats) {
        for (int i = 0; i < floats.length; i++) {
            if (i < coordinates.size())
                coordinates.set(i, floats[i]);
            else
                coordinates.add(floats[i]);
        }
    }

    /**
     * Get float [ ].
     *
     * @return the float [ ]
     */
    public float[] get() {
        float[] result = new float[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++)
            result[i] = coordinates.get(i);
        return result;
    }

    public float getDistance() {
        return getDistance(new Vector());
    }

    public float getDistance(Vector position) {
        float result = 0;
        for (int i = 0; i < coordinates.size() || i < position.coordinates.size(); i++)
            result += Math.pow(position.get(i) - get(i), 2);

        result = ((float) Math.sqrt(result));
        return result;
    }

    public float getCoordinatesSum() {
        float result = 0;
        for (Float coordinate : coordinates)
            result += coordinate;
        return result;
    }

    public static Vector add(Vector a, Vector b) {
        Vector result = a.clone();
        return result.add(b);
    }

    public static Vector sub(Vector a, Vector b) {
        Vector result = a.clone();
        return result.sub(b);
    }

    public static Vector mult(Vector a, float b) {
        Vector result = a.clone();
        return result.mult(b);
    }

    public static Vector divide(Vector a, float b) {
        Vector result = a.clone();
        return result.divide(b);
    }

    public static Vector norm(Vector a) {
        Vector result = a.clone();
        return result.norm();
    }

    public static Vector normCoords(Vector a) {
        Vector result = a.clone();
        float max = 0;
        for (float c : result.coordinates)
            max = c > max ? c : max;
        return result.divide(max);
    }

    public static Vector cross(Vector u, Vector v) {
        float uvi, uvj, uvk;
        uvi = u.get(1) * v.get(2) - v.get(1) * u.get(2);
        uvj = v.get(0) * u.get(2) - u.get(0) * v.get(2);
        uvk = u.get(0) * v.get(1) - v.get(0) * u.get(1);
        return new Vector(uvi, uvj, uvk).norm();
    }


    @Override
    public int compareTo(@NonNull Vector o) {
        int a = (int) (getCoordinatesSum() * 1000);
        int b = (int) (o.getCoordinatesSum() * 1000);
        return a < b ? -1 : a > b ? 1 : 0;
    }

    @Override
    public String toString() {
        String result = "";
        for (float f : coordinates)
            result += ", " + f;
        return result.length() == 0 ? "[0]" : "[" + result.substring(2) + "]";
    }

    @Override
    public Vector clone() {
        return new Vector(get());
    }

}
