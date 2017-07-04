package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;

import java.util.Random;

public class EnemyBody extends CarBody {


    private GameObject3D carBody;
    private int counter = 0;
    private boolean isTurningLeft = false;
    private boolean isTurningRight = false;

    public EnemyBody(Context mActivityContext, int modelID, int textureId) {
        super(mActivityContext, modelID, textureId);
        carBody = super.getObject3D();
        counter = new Random().nextInt(120);
    }

    @Override
    public void runs() {
        carBody.rotate(new Vector(1, 0, 0, -90));
        carBody.rotate(new Vector(0, 1, 0, angle * rotateSpeed));
        resetRotation();
        counter++;
    }

    public void runsWithSpeed(float speed) {
        carBody.move(new Vector(0, -speed, 0));
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isTurningLeft() {
        return isTurningLeft;
    }

    public void setTurningLeft(boolean turningLeft) {
        isTurningLeft = turningLeft;
    }

    public boolean isTurningRight() {
        return isTurningRight;
    }

    public void setTurningRight(boolean turningRight) {
        isTurningRight = turningRight;
    }


}
