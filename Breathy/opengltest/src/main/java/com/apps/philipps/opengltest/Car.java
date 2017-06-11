package com.apps.philipps.opengltest;


import android.content.Context;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;

public class Car {

    private GameObject3D car;
    private float xPosition = 0.0f;
    private float limit = 0.5f;
    private float speed = 0f;
    private float delay = 2000f;
    float angle = 0;
    float rotateSpeed = 360f;

    public Car(Context mActivityContext, int modelID, int textureId) {
        car = new GameObject3D(GameObject3D.loadObject(mActivityContext, modelID, textureId));
    }

    public void setPosition(Vector position) {
        car.setPosition(position);
    }

    public void setRotation(Vector rotation) {
        car.setRotation(rotation);
    }

    public void turnLeft(float dx) {
        if (xPosition > -limit) {
            speed = Math.abs(dx) / delay;
            xPosition -= speed;
            car.getPosition().add(new Vector(-speed, 0, 0));
            angle -= speed;
        }
    }

    public void turnRight(float dx) {
        if (xPosition < limit) {
            speed = Math.abs(dx) / delay;
            xPosition += speed;
            car.getPosition().add(new Vector(speed, 0, 0));
            angle += speed;
        }
    }


    public void runs() {
        car.move(new Vector(0, 0, 0));
        car.rotate(new Vector(1, 0, 0, -90));
        car.rotate(new Vector(0, 1, 0, angle * rotateSpeed));
        resetRotation();
        //car.rotate(new Vector(0, 0, 1, angle));
    }

    public void resetRotation() {
        if (Math.abs(angle) < speed)
            angle = 0f;
        if (angle > 0)
            angle -= speed;
        else if (angle < 0)
            angle += speed;
    }


    public void draw(long deltaTime) {
        car.update(deltaTime);
    }

    public GameObject3D getObject3D() {
        return car;
    }
}
