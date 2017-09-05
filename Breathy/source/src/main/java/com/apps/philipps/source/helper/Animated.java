package com.apps.philipps.source.helper;

import android.provider.Settings;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.interfaces.IObserver;

import java.security.PolicySpi;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 18.03.2017. Project Breathy
 */
public class Animated {

    protected final String TAG = getClass().getSimpleName();
    private Vector position;
    private Vector destination;
    private List<IObserver> oberver = new ArrayList<>();
    private double speed;

    private boolean active = false;

    /**
     * Instantiates a new Animated.
     *
     * @param position the position
     */
    public Animated(@NonNull Vector position) {
        this.position = position;
    }


    /**
     * Instantiates a new Animated.
     *
     * @param position    the position
     * @param destination the destination
     */
    public Animated(@NonNull Vector position, @NonNull Vector destination) {
        this.position = position;
        this.destination = destination;
    }

    public boolean isMoving() {
        return active;
    }

    /**
     * Instantiates a new Animated.
     *
     * @param position    the position
     * @param destination the destination
     * @param speed       the speed
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
    public void resume() {
        active = true;
    }

    public void resume(double speed) {
        this.speed = speed;
        active = destination != null && speed != 0;
    }

    /**
     * Animate with previeous speed
     *
     * @param destination
     */
    public void animate(Vector destination) {
        this.destination = destination;
        active = speed != 0;
    }

    /**
     * Animate.
     *
     * @param destination the destination
     * @param speed       the speed
     */
    public void animate(Vector destination, double speed) {
        this.speed = speed;
        animate(destination);
    }

    /**
     * Get position vector.
     *
     * @return the vector
     */
    public Vector getPosition() {
        return position;
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
        return destination;
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
    public void setPosition(Vector position) {
        this.position = position;
        if (destination.compareTo(position) == 0)
            active = false;
    }

    public void update(long deltaMilliseconds) {
        if (active) {
            if (position.compareTo(destination) == 0)
                active = false;
            else {
                Vector div = Vector.sub(destination, position);
                Vector norm = div.clone().norm();
                Vector add = Vector.mult(norm, speed * deltaMilliseconds / 1000f);
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
