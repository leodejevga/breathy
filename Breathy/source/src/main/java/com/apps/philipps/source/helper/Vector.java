package com.apps.philipps.source.helper;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 17.03.2017. Project Breathy
 */
public class Vector implements Comparable<Vector>, Cloneable{

    private List<Float> coordinates;

    /**
     * Instantiates a new Vector.
     *
     * @param coordinates the coordinates
     */
    public Vector(Float... coordinates){
        this.coordinates = new ArrayList<>();
        for(Float coordinate : coordinates)
            if(coordinate!=null)
                this.coordinates.add(coordinate);
    }

    /**
     * Add position.
     *
     * @param position the position
     * @return the position
     */
    public Vector add(Vector position){
        for (int i = 0; i < this.coordinates.size() && i < position.coordinates.size(); i++)
            this.coordinates.set(i, this.coordinates.get(i) + position.coordinates.get(i));
        if(this.coordinates.size()<position.coordinates.size())
            for (int i = this.coordinates.size(); i < position.coordinates.size(); i++)
                this.coordinates.add(position.coordinates.get(i));
        return this;
    }

    /**
     * Sub position.
     *
     * @param position the position
     * @return the position
     */
    public Vector sub(Vector position){
        for (int i = 0; i < this.coordinates.size() && i < position.coordinates.size(); i++)
            this.coordinates.set(i, this.coordinates.get(i) - position.coordinates.get(i));
        if(this.coordinates.size()<position.coordinates.size())
            for (int i = this.coordinates.size(); i < position.coordinates.size(); i++)
                this.coordinates.add(-position.coordinates.get(i));
        return this;
    }

    /**
     * Mult position.
     *
     * @param multiplier the multiplier
     * @return the position
     */
    public Vector mult(float multiplier){
        for (int i = 0; i < this.coordinates.size(); i++)
            this.coordinates.set(i, this.coordinates.get(i) * multiplier);
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
    public Vector mult(int start, int end, float multiplier){
        for (int i = start; i < this.coordinates.size() && i <= end; i++)
            this.coordinates.set(i, this.coordinates.get(i) * multiplier);
        return this;
    }

    /**
     * Mult position.
     *
     * @param coordinate the coordinate
     * @param multiplier the multiplier
     * @return the position
     */
    public Vector mult(int coordinate, float multiplier){
        this.coordinates.set(coordinate, this.coordinates.get(coordinate) * multiplier);
        return this;
    }

    /**
     * Divide position.
     *
     * @param divisor the divisor
     * @return the position
     */
    public Vector divide(float divisor){
        if(divisor == 0)
            return null;
        for (int i = 0; i < this.coordinates.size(); i++)
            this.coordinates.set(i, this.coordinates.get(i) / divisor);
        return this;
    }

    public Vector norm(){
        return divide(getDistance());
    }

    /**
     * Get float.
     *
     * @param coordinate the coordinate
     * @return the float
     */
    public Float get(int coordinate){
        if(coordinate<coordinates.size())
            return coordinates.get(coordinate);
        else return null;
    }

    /**
     * Get float [ ].
     *
     * @return the float [ ]
     */
    public Float[] get(){
        Float[] result = new Float[coordinates.size()];
        coordinates.toArray(result);
        return result;
    }

    public float getDistance(){
        return getDistance(new Vector());
    }
    public float getDistance(Vector position){
        float result=0;
        for (int i = 0; i < coordinates.size() || i < position.coordinates.size(); i++) {
            if(i<coordinates.size() && i<position.coordinates.size())
                result += Math.pow(position.get(i) - get(i), 2);
            else if(i<coordinates.size())
                result += get(i) * get(i);
            else
                result += position.get(i) * position.get(i);
        }
        result = ((float) Math.sqrt(result));
        return result;
    }
    public float getCoordinatesSum(){
        float result = 0;
        for(Float coordinate : coordinates)
            result+=coordinate;
        return result;
    }

    public static Vector add(Vector a, Vector b){
        Vector result = a.clone();
        return result.add(b);
    }

    public static Vector sub(Vector a, Vector b){
        Vector result = a.clone();
        return result.sub(b);
    }

    public static Vector mult(Vector a, float b){
        Vector result = a.clone();
        return result.mult(b);
    }

    public static Vector divide(Vector a, float b){
        Vector result = a.clone();
        return result.divide(b);
    }
    public static Vector norm(Vector a){
        Vector result = a.clone();
        return result.norm();
    }


    @Override
    public int compareTo(@NonNull Vector o) {
        int a = (int)(getCoordinatesSum()*1000);
        int b = (int)(o.getCoordinatesSum()*1000);
        return a<b?-1:a>b?1:0;
    }

    @Override
    public String toString() {
        String result="";
        for (int i = 0; i < coordinates.size(); i++) {
            result += i==0?"X: ":i==1?"Y: ":i==2?"Z: ":(i+1) + ": ";
            result += coordinates.get(i) + " ";
        }
        return result;
    }

    @Override
    public Vector clone() {
        return new Vector(get());
    }

}
