package com.breathy.racing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Jürgen on 03.07.2017.
 */

public class SlowerUpdate {

    private float waitsPerMin = 30;
    private float curWait=0;

    public SlowerUpdate(int fps, int callsPerMin){
        this.waitsPerMin= fps / callsPerMin;
    }

    public boolean update( ){
        curWait++;
        if(curWait >= waitsPerMin) {
            curWait -= waitsPerMin;
            return true;
        }
        return false;
    }
}
