package com.breathy.racing;

import android.util.Log;
import android.util.Pair;

import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.BreathInterpreter;
import com.apps.philipps.source.PlanManager;

/**
 * Created by Jürgen on 03.07.2017.
 */

public class RaceBreathInterpreter {

    private final int dist;
    private final Pair minMax;
    private int punishProzent = 0;
    private int curBostPoints = 0;


    public RaceBreathInterpreter( Pair<Integer,Integer> minMax){
        this.minMax = minMax;
        this.dist = minMax.second -minMax.first;
        BreathData.get( 0 );
    }



    public void update(){

        int plannedBreath = BreathInterpreter.getStatus().getError().value;
        Log.i("Bewegen",plannedBreath +"");
        plannedBreath = (int) (Math.random() * 6); //TODO
        switch ( plannedBreath ){
            case 0: punishProzent = Math.max( 0, punishProzent - 10 );
                break;
            case 1: punishProzent = Math.max( 0, punishProzent - 5 );
                break;
            case 2: punishProzent = Math.max( 0, punishProzent - 1 );
                break;
            case 4: punishProzent = Math.min( 100, punishProzent + 10 );
                break;
            case 5: punishProzent = Math.min( 100, punishProzent + 15 );
                break;
            case 6: punishProzent = Math.min( 100, punishProzent + 20 );
                break;
            default:
                return;
        }
        curBostPoints = Math.max( 0, curBostPoints - (plannedBreath - 3));
    }

    public int getCurY(){
        return (int) minMax.second - dist / 100 * punishProzent;
    }

    public void setCurBostPoints(int value){
        this.curBostPoints = value;
    }

    public int getCurBostPoints(){
        return curBostPoints;
    }
}
