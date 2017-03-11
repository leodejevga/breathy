package com.apps.philipps.app.simulator;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.SaveData;

/**
 * Created by Jevgenij Huebert on 11.03.2017. Project Breathy
 */

public class Simulator {
    private boolean initialized = false;
    private int rate;
    private Integer[] data;
    public static final String DATA_NAME="Breathy_Simulator_Data";
    private static Simulator simulator = new Simulator();

    private Simulator(){
        rate = 2;
    }

    public Simulator init(Context context, Integer... rate){
        if (!initialized) {
            simulator.data = new SaveData<Integer[]>(context).readObject(DATA_NAME);
            simulator.startSimulation();
            simulator.rate = rate.length>0?rate[0]:simulator.rate;
            simulator.initialized = true;
        }
        return simulator;
    }

    public static Simulator getSimulator(){
        return simulator;
    }





    public boolean connect(){
        if(initialized && AppState.btState != AppState.BtState.Connected)
            startSimulation();
        return AppState.btState == AppState.BtState.Connected;
    }

    public boolean disconnect(){
        if(initialized)
            AppState.btState = AppState.BtState.Enabled;
        return initialized;
    }

    private void startSimulation() {
        AppState.btState = AppState.BtState.Connected;

        new Thread(new Runnable() {
            private long start = System.currentTimeMillis();
            private int index = 0;
            @Override
            public void run() {
                while(AppState.btState == AppState.BtState.Connected && data!=null && data.length>0){
                    if(System.currentTimeMillis() - start >= 1000/rate)
                        BreathData.add(data[index++]);
                    index = index%data.length;
                }
                AppState.btState = AppState.BtState.Enabled;
            }
        }).start();
    }
}
