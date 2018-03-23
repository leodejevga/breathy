package com.apps.philipps.source.helper;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 17.03.2017. Project Breathy
 */
public class Vector implements Comparable<Vector>, Cloneable {

    private List<Double> coordinates;
    public Vector(Vector v, double... coordinates) {
        this.coordinates = new ArrayList<>();
        for (double coordinate : v.get())
            this.coordinates.add(coordinate);

        for (double coordinate : coordinates)
            this.coordinates.add(coordinate);
    }


    public Vector(Vector v, float... coordinates) {
        this.coordinates = new ArrayList<>();
        for (double coordinate : v.get())
            this.coordinates.add(coordinate);

        for (double coordinate : coordinates)
            this.coordinates.add(coordinate);
    }

    /**
     * Instantiates a new Vector with doubles.
     *
     * @param coordinates the coordinates
     */
    public Vector(double... coordinates) {
        this.coordinates = new ArrayList<>();
        for (double coordinate : coordinates)
            this.coordinates.add(coordinate);
    }

    /**
     * Instantiates a new Vector with floats.
     *
     * @param coordinates the coordinates
     */
    public Vector(float... coordinates) {
        this.coordinates = new ArrayList<>();
        for (double coordinate : coordinates)
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
        double a = get(0);
        double b = position.get(0);
        double ab = a+b;
        double add = get(0) + position.get(0);
        for (int i = 0; i < getDimensions() || i < position.getDimensions(); i++)
            set(i, get(i) + position.get(i));
        return this;
    }

    public Vector add(double... position) {
        Vector toAdd = new Vector(position);
        for (int i = 0; i < getDimensions() || i < toAdd.getDimensions(); i++)
            set(i, get(i) + toAdd.get(i));
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

    public Vector sub(double... position) {
        Vector toSub = new Vector(position);
        for (int i = 0; i < getDimensions() || i < toSub.getDimensions(); i++)
            set(i, get(i) - toSub.get(i));
        return this;
    }

    /**
     * Mult position.
     *
     * @param multiplier the multiplier
     * @return the position
     */
    public Vector mult(double multiplier) {
        for (int i = 0; i < getDimensions(); i++)
            set(i, get(i) * multiplier);
        return this;
    }

    /**
     * Mult position.
     *
     * @param start      the position
     * @param end        the destination
     * @param multiplier the multiplier
     * @return the position
     */
    public Vector mult(int start, int end, double multiplier) {
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
    public Vector mult(int coordinate, double multiplier) {
        set(coordinate, get(coordinate) * multiplier);
        return this;
    }

    /**
     * Divide position.
     *
     * @param divisor the divisor
     * @return the position
     */
    public Vector divide(double divisor) {
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
        double max = 0;
        for (double c : coordinates)
            max = c > max ? c : max;
        return divide(max == 0 ? 1 : max);
    }

    /**
     * Get double [ ].
     *
     * @return the double [ ]
     */
    public double[] get() {
        double[] result = new double[getDimensions()];
        for (int i = 0; i < getDimensions(); i++)
            result[i] = get(i);
        return result;
    }

    public float[] getF() {
        float[] result = new float[getDimensions()];
        for (int i = 0; i < getDimensions(); i++)
            result[i] = getF(i);
        return result;
    }

    /**
     * Get double.
     *
     * @param coordinate the coordinate
     * @return the double
     */
    public double get(int coordinate) {
        if (coordinate < getDimensions())
            return coordinates.get(coordinate);
        else return 0;
    }

    public float getF(int coordinate) {
        return (float) get(coordinate);
    }

    public void set(Vector v) {
        for (int i = 0; i < v.getDimensions(); i++) {
            if (i < coordinates.size())
                coordinates.set(i, v.get(i));
            else
                coordinates.add(v.get(i));
        }
    }

    public void set(int index, double data) {
        if (getDimensions() > index)
            coordinates.set(index, data);
        else {
            for (int i = getDimensions(); i < index; i++)
                coordinates.add(0d);
            coordinates.add(data);
        }
    }

    public void set(double... doubles) {
        for (int i = 0; i < doubles.length; i++) {
            if (i < getDimensions())
                coordinates.set(i, doubles[i]);
            else
                coordinates.add(doubles[i]);
        }
    }


    public double getDistance() {
        return getDistance(new Vector());
    }

    public double getDistance(Vector position) {
        double result = 0;
        for (int i = 0; i < getDimensions() || i < position.getDimensions(); i++)
            result += Math.pow(position.get(i) - get(i), 2);

        result = ((double) Math.sqrt(result));
        return result;
    }

    public double getCoordinatesSum() {
        double result = 0;
        for (double coordinate : coordinates)
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

    public static Vector mult(Vector a, double b) {
        Vector result = a.clone();
        return result.mult(b);
    }

    public static Vector divide(Vector a, double b) {
        Vector result = a.clone();
        return result.divide(b);
    }

    public static Vector norm(Vector a) {
        Vector result = a.clone();
        return result.norm();
    }

    public static Vector normCoords(Vector a) {
        Vector result = a.clone();
        double max = 0;
        for (double c : result.coordinates)
            max = c > max ? c : max;
        return result.divide(max);
    }

    public static Vector cross(Vector u, Vector v) {
        double uvi, uvj, uvk;
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
        for (double f : coordinates)
            result += ", " + f;
        return result.length() == 0 ? "[0]" : "[" + result.substring(2) + "]";
    }

    @Override
    public Vector clone() {
        return new Vector(get());
    }

}
