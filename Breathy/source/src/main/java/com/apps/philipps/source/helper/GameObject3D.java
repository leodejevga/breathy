package com.apps.philipps.source.helper;

import com.apps.philipps.source.interfaces.IGameObject;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Created by Jevgenij Huebert on 23.03.2017. Project Breathy
 */

public class GameObject3D implements IObserver, IGameObject {



    @Override
    public void call(Object... messages) {

    }

    @Override
    public void setPosition(Vector position) {

    }

    @Override
    public Vector getPosition() {
        return null;
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public void move(Vector destination, int speed) {

    }

    @Override
    public void move(Vector destination) {

    }

    @Override
    public void move(int speed) {

    }

    @Override
    public void update(long deltaMilliseconds) {

    }

    @Override
    public boolean intersect(IGameObject gameObject) {
        return false;
    }

    @Override
    public Vector getBoundaries() {
        return null;
    }
}
