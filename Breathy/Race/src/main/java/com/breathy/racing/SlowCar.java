package com.breathy.racing;

import android.support.annotation.NonNull;
import android.view.View;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.GameObject2D;

/**
 * Created by Jürgen on 10.05.2017.
 */

public class SlowCar extends GameObject2D {

    int curXStreet;
    double groundSpeed;

    public SlowCar( @NonNull View object, Vector position, Vector destination, int curXStreet, double groundspeed) {
        super( object, position, destination );
        this.curXStreet = curXStreet;
        this.groundSpeed = groundspeed;
    }

    public int getCurXStreet(){
        return curXStreet;
    }

    public void setCurXStreet(int xStreet){
        this.curXStreet = xStreet;
    }

    public double getGroundSpeed(){
        return groundSpeed;
    }
}
