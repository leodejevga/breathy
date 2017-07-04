package com.apps.philipps.opengltest;


import android.content.Context;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;

public class CarBody {

    private GameObject3D carBody;
    public float speed = 0f;
    float angle = 0;
    float rotateSpeed = 360f;
    float constant = 0f;
    Car car;

    public CarBody(Context mActivityContext, int modelID, int textureId) {
        this.car = car;
        carBody = new GameObject3D(GameObject3D.loadObject(mActivityContext, modelID, textureId));
    }

    public void setPosition(Vector position) {
        carBody.setPosition(position);
    }

    public void setRotation(Vector rotation) {
        carBody.setRotation(rotation);
    }

    public void turnRight(float speed) {
        this.speed = speed;
        carBody.getPosition().add(new Vector(-speed, 0, 0));
        angle -= speed;
    }

    public void turnLeft(float speed) {
        this.speed = speed;
        carBody.getPosition().add(new Vector(speed, 0, 0));
        angle += speed;
    }


    public void runs() {
        carBody.move(new Vector(0, 0, 0));
        carBody.rotate(new Vector(1, 0, 0, -90));
        carBody.rotate(new Vector(0, 1, 0, angle * rotateSpeed));
        resetRotation();
        constant = 0.0f;
        //carBody.rotate(new Vector(0, 0, 1, angle));
    }

    public void crashes() {
        constant += 5.0f;
        carBody.move(new Vector(0, 0, 0));
        carBody.rotate(new Vector(1, 0, 0, -90));
        carBody.rotate(new Vector(0, 1, 0, constant));
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
        carBody.update(deltaTime);
    }

    public GameObject3D getObject3D() {
        return carBody;
    }
}
