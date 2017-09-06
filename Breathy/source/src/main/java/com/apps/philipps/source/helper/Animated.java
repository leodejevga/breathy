package com.apps.philipps.source.helper;

import android.support.annotation.NonNull;

import com.apps.philipps.source.interfaces.IObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 18.03.2017. Project Breathy
 */
public class Animated {

    protected final String TAG = getClass().getSimpleName();
    private Vector start;
    private Vector end;
    private List<IObserver> oberver = new ArrayList<>();
    private double speed;
    private double factor = 1;

    private boolean active = false;

    /**
     * Instantiates a new Animated.
     *
     * @param start the start
     */
    public Animated(@NonNull Vector start) {
        this.start = start;
    }


    /**
     * Instantiates a new Animated.
     *
     * @param start    the start
     * @param end the end
     */
    public Animated(@NonNull Vector start, @NonNull Vector end) {
        this.start = start;
        this.end = end;
    }

    public boolean isMoving() {
        return active;
    }

    /**
     * Instantiates a new Animated.
     *
     * @param start    the start
     * @param end the end
     * @param speed       the speed
     * @param activate    the activate
     */
    public Animated(@NonNull Vector start, @NonNull Vector end, double speed, boolean activate) {
        this.start = start;
        this.end = end;
        this.speed = speed;
        this.active = activate;
    }

    /**
     * Stop.
     */
    public void stop() {
        active = false;
    }

    /**
     * Resume.
     */
    public void resume() {
        active = true;
    }

    public void resume(double speed) {
        this.speed = speed;
        active = end != null && speed != 0;
    }

    /**
     * Animate with previeous speed
     *
     * @param end
     */
    public void animate(Vector end) {
        this.end = end;
        active = speed != 0;
    }

    /**
     * Animate.
     *
     * @param destination the end
     * @param speed       the speed
     */
    public void animate(Vector destination, double speed) {
        this.speed = speed;
        animate(destination);
    }

    /**
     * Get start vector.
     *
     * @return the vector
     */
    public Vector getStart() {
        return start;
    }

    /**
     * Get start vector.
     *
     * @return the vector
     */
    public void setFactor(double factor) {
        this.factor = factor;
    }

    /**
     * Add observer.
     *
     * @param observer the observer
     */
    public void addObserver(IObserver observer) {
        this.oberver.add(observer);
    }

    /**
     * Get end vector
     *
     * @return the end as vector
     */
    public Vector getEnd() {
        return end;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Set end vector
     *
     * @param vector end as vector
     */
    public void setEnd(Vector vector) {
        this.end = vector;
        if (end.compareTo(start) == 0)
            active = false;
    }

    /**
     * Position of Animated Object
     *
     * @param start
     */
    public void setStart(Vector start) {
        this.start = start;
        if (end.compareTo(start) == 0)
            active = false;
    }

    public void update(long deltaMilliseconds) {
        if (active) {
            if (start.compareTo(end) == 0)
                active = false;
            else {
                Vector div = Vector.sub(end, start);
                Vector norm = div.clone().norm();
                Vector add = Vector.mult(norm, speed * deltaMilliseconds / 1000f * factor);
                if (start.getDistance(end) < start.getDistance(Vector.add(start, add))) {
                    start = end.clone();
                    active = false;
                } else {
                    start.add(add);
                    for (IObserver o : oberver)
                        o.call(start);
                }
            }
        }
    }

    @Override
    public String toString() {
        return start + " --> " + end + (isMoving() ? "  is moving with " + speed : "");
    }

    @Override
    public Animated clone() {
        return new Animated(start.clone(), end.clone(), speed, active);
    }
}
