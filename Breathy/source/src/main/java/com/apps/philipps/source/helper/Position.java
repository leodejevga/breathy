package com.apps.philipps.source.helper;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 17.03.2017. Project Breathy
 */
public class Position implements Comparable<Position>{

    private List<Double> coordinates;

    /**
     * Instantiates a new Position.
     *
     * @param coordinates the coordinates
     */
    public Position(double... coordinates){
        this.coordinates = new ArrayList<>();
        for(Double coordinate : coordinates){
            this.coordinates.add(coordinate);
        }
    }

    /**
     * Add position.
     *
     * @param position the position
     * @return the position
     */
    public Position add(Position position){
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
    public Position sub(Position position){
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
    public Position mult(double multiplier){
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
    public Position mult(int start, int end, double multiplier){
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
    public Position mult(int coordinate, double multiplier){
        this.coordinates.set(coordinate, this.coordinates.get(coordinate) * multiplier);
        return this;
    }

    /**
     * Divide position.
     *
     * @param divisor the divisor
     * @return the position
     */
    public Position divide(double divisor){
        if(divisor == 0)
            return null;
        for (int i = 0; i < this.coordinates.size(); i++)
            this.coordinates.set(i, this.coordinates.get(i) / divisor);
        return this;
    }

    /**
     * Get double.
     *
     * @param coordinate the coordinate
     * @return the double
     */
    public double get(int coordinate){
        return coordinates.get(coordinate);
    }

    /**
     * Get double [ ].
     *
     * @return the double [ ]
     */
    public Double[] get(){
        Double[] result = new Double[coordinates.size()];
        coordinates.toArray(result);
        return result;
    }

    public double getDistance(){
        return getDistance(new Position());
    }
    public double getDistance(Position position){
        double result=0;
        for (int i = 0; i < coordinates.size() || i < position.coordinates.size(); i++) {
            if(i<coordinates.size() && i<coordinates.size())
                result += Math.pow(position.get(i) - get(i), 2);
            else if(i<coordinates.size())
                result += get(i) * get(i);
            else
                result += position.get(i) * position.get(i);
        }
        result = Math.sqrt(result);
        return result;
    }
    public double getCoordinatesSum(){
        double result = 0;
        for(Double coordinate : coordinates)
            result+=coordinate;
        return result;
    }

    @Override
    public int compareTo(@NonNull Position o) {
        double a = getCoordinatesSum();
        double b = o.getCoordinatesSum();
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
}
