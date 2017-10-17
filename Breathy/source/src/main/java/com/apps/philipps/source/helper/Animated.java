package com.apps.philipps.source.helper;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.apps.philipps.source.interfaces.IObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 18.03.2017. Project Breathy
 */
public class Animated implements Cloneable {

    protected final String TAG = getClass().getSimpleName();
    protected Vector position;
    protected Vector destination;
    private List<IObserver> oberver = new ArrayList<>();
    private double speed;
    protected double factor = 1;

    private boolean active = false;

    /**
     * Instantiates a new Animated.
     *
     * @param position the position
     */
    public Animated(@NonNull Vector position) {
        this(position, position.clone());

    }


    /**
     * Instantiates a new Animated.
     *
     * @param position    the position
     * @param destination the destination
     */
    public Animated(@NonNull Vector position, @NonNull Vector destination) {
        this(position, destination, 0, false);
    }

    public boolean isMoving() {
        return active;
    }

    /**
     * Instantiates a new Animated.
     *
     * @param position    the position
     * @param destination the destination
     * @param speed       the speed defines pixel per second
     * @param activate    the activate
     */
    public Animated(@NonNull Vector position, @NonNull Vector destination, double speed, boolean activate) {
        this.position = position;
        this.destination = destination;
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
    public void move() {
        active = speed != 0 || destination.compareTo(position) == 0;
    }

    public void move(double speed) {
        move(destination, speed);
    }

    /**
     * Animate with previeous speed
     *
     * @param destination
     */
    public void move(Vector destination) {
        this.destination = destination;
        active = speed != 0 && destination.compareTo(position) != 0;
    }

    /**
     * Animate.
     *
     * @param destination the destination
     * @param speed       the speed
     */
    public void move(Vector destination, double speed) {
        this.speed = speed;
        move(destination);
    }

    /**
     * Get position vector.
     *
     * @return the vector
     */
    public Vector getPosition() {
        return position.clone();
    }

    /**
     * Get position vector.
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
     * Get destination vector
     *
     * @return the destination as vector
     */
    public Vector getDestination() {
        return destination.clone();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Set destination vector
     *
     * @param vector destination as vector
     */
    @CallSuper
    public void setDestination(Vector vector) {
        this.destination = vector;
        if (destination.compareTo(position) == 0)
            active = false;
    }

    /**
     * Position of Animated Object
     *
     * @param position
     */
    @CallSuper
    public void setPosition(Vector position) {
        this.position = position;
        if (destination.compareTo(position) == 0)
            active = false;
    }

    @CallSuper
    public void update(double delta) {
        if (active) {
            if (position.compareTo(destination) == 0)
                active = false;
            else {
                Vector div = Vector.sub(destination, position);
                Vector norm = div.clone().norm();
                Vector add = Vector.mult(norm, speed * delta / 1000f * factor);
                if (position.getDistance(destination) < position.getDistance(Vector.add(position, add))) {
                    position = destination.clone();
                    active = false;
                } else {
                    position.add(add);
                    for (IObserver o : oberver)
                        o.call(position);
                }
            }
        }
    }

    @Override
    public String toString() {
        return position + " --> " + destination + (isMoving() ? "  is moving with " + speed : "");
    }

    @Override
    public Animated clone() {
        return new Animated(position.clone(), destination.clone(), speed, active);
    }
}
