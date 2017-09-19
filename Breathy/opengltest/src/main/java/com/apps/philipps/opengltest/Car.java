package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;

import java.util.ArrayList;


public class Car {
    private CarBody carBody;
    private ArrayList<Tire> tires = new ArrayList<>();
    private float limit = GameEngine.streetSize / 2f;
    //private float speed = 0f;
    //private float delay = 3000f;
    private float xPosition = 0.0f;

    public void setCarBodyModel(Context mActivityContext, int modelID, int textureId) {
        carBody = new CarBody(mActivityContext, modelID, textureId);
    }

    public void setCarTireModel(Context mActivityContext, int modelID, int textureId, boolean isFrontTire) {
        Tire tire = new Tire(mActivityContext, this, modelID, textureId, isFrontTire);
        tires.add(tire);
    }

    public void setCarBodyPosition(Vector position) {
        carBody.setPosition(position);
    }

    public void setCarBodyRotation(Vector rotation) {
        carBody.setRotation(rotation);
    }

    public void setTirePosition(Vector position) {
        for (Tire tire : tires)
            tire.setPosition(position);
    }

    public void setTireRotation(Vector rotation) {
        for (Tire tire : tires)
            carBody.setRotation(rotation);
    }

    public void turnRight(float dx) {
        if (xPosition < limit) {
            xPosition += dx;
            carBody.turnLeft(dx);
            for (Tire tire : tires)
                tire.turnRight(dx);
        }
    }

    public void turnLeft(float dx) {
        if (xPosition > -limit) {
            xPosition -= dx;
            carBody.turnRight(dx);
            for (Tire tire : tires)
                tire.turnLeft(dx);
        }
    }


    public void runsWithSpeed(float speed) {
        carBody.runs();
        for (Tire tire : tires)
            tire.runs(speed);
    }

    public void crashes() {
        carBody.crashes();
        for (Tire tire : tires)
            tire.crashes();
    }


    public void draw(long deltaTime) {
        carBody.draw(deltaTime);
        for (Tire tire : tires)
            tire.draw(deltaTime);
    }

    public GameObject3D getCarBodyObject3D() {
        return carBody.getObject3D();
    }

    public ArrayList<GameObject3D> getTiresObject3D() {
        ArrayList<GameObject3D> tiresObject3D = new ArrayList<>();
        for (Tire tire : tires)
            tiresObject3D.add(tire.getObject3D());
        return tiresObject3D;
    }

    public void drawBoundingBoxLines() {
        carBody.getObject3D().getBoundingBox().drawLines();
    }

    public void renewBoundingBoxPosition() {
        carBody.getObject3D().getBoundingBox().renewLinesPosition();
    }
}
