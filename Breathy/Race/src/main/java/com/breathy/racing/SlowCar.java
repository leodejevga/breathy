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
    int groundSpeed;

    public SlowCar( @NonNull View object, Vector position, Vector destination, int curXStreet) {
        super( object, position, destination );
        this.curXStreet = curXStreet;
    }

    public int getCurXStreet(){
        return curXStreet;
    }

    public void setCurXStreet(int xStreet){
        this.curXStreet = xStreet;
    }


}
