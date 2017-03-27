package com.apps.philipps.source.interfaces;

import com.apps.philipps.source.helper.Vector;

/**
 * Created by Jevgenij Huebert on 23.03.2017. Project Breathy
 */

public interface IGameObject {
    void setPosition(Vector position);
    Vector getPosition();
    boolean isMoving();
    void move(Vector destination, int speed);
    void move(Vector destination);
    void move(int speed);
    void update(long deltaMilliseconds);
    boolean intersect(IGameObject gameObject);
    Vector getBoundaries();

}
