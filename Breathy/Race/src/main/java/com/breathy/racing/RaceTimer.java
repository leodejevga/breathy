package com.breathy.racing;

/**
 * Created by Jürgen on 17.10.2017.
 */

public class RaceTimer {

    int i;

    public RaceTimer(int i){
        this.i=i;
    }

    public boolean update(){
        i--;
        if(i<0){
            return false;
        }
        return true;
    }
    public void setI(int i){
        this.i=i;
    }
}
