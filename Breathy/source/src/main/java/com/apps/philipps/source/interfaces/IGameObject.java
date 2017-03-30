package com.apps.philipps.source.interfaces;

import com.apps.philipps.source.helper.Vector;

/**
 * Created by Jevgenij Huebert on 23.03.2017. Project Breathy
 */
public interface IGameObject {
    /**
     * Sets position.
     *
     * @param position the position
     */
    void setPosition(Vector position);

    /**
     * Sets rotation.
     *
     * @param rotation the rotation
     */
    void setRotation(Vector rotation);

    /**
     * Gets position.
     *
     * @return the position
     */
    Vector getPosition();

    /**
     * Is moving boolean.
     *
     * @return the boolean
     */
    boolean isMoving();

    /**
     * Move.
     *
     * @param destination the destination
     * @param speed       the speed
     */
    void move(Vector destination, int speed);

    /**
     * Move.
     *
     * @param destination the destination
     */
    void move(Vector destination);

    /**
     * Move.
     *
     * @param speed the speed
     */
    void move(int speed);

    /**
     * Update.
     *
     * @param deltaMilliseconds the delta milliseconds
     */
    void update(long deltaMilliseconds);

    /**
     * Intersect boolean.
     *
     * @param gameObject the game object
     * @return the boolean
     */
    boolean intersect(IGameObject gameObject);

    /**
     * Gets boundaries.
     *
     * @return the boundaries
     */
    Vector getBoundaries();

}
