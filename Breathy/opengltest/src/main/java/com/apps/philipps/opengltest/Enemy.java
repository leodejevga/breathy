package com.apps.philipps.opengltest;

import com.apps.philipps.source.helper.Vector;

import java.util.Random;

public class Enemy extends Car {
    private int counter = 0;
    private int counterRange = 120;
    private boolean isTurningLeft = false;
    private boolean isTurningRight = false;


    public Enemy() {
        counter = new Random().nextInt(counterRange);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setNewCounter() {
        this.counter = new Random().nextInt(counterRange);
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

    public void runsWithSpeed(float speed) {
        this.setCarBodyPosition(this.getCarBodyObject3D().getPosition().add(new Vector(0, -speed, 0)));
        super.runsWithSpeed(speed);
    }

}
