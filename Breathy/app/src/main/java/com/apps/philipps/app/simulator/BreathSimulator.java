package com.apps.philipps.app.simulator;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.SaveData;
import com.apps.philipps.source.interfaces.IObserver;

import java.util.Random;

/**
 * Created by Jevgenij Huebert on 11.03.2017. Project Breathy
 */

public class BreathSimulator {
    private boolean recording = false;
    private boolean initialized = false;
    private int rate;
    private SaveData<Integer[]> saveData;
    private Integer[] data;
    private static final String DATA_NAME = "Breathy_Simulator_Data";
    private static final String TAG = "BreathSimulator";
    private static BreathSimulator simulator = new BreathSimulator();

    private BreathSimulator() {
        rate = 2;
    }

    public BreathSimulator init(Context context, Integer... rate) {
        if (!initialized) {
            saveData = new SaveData<>(context);
            data = saveData.readObject(DATA_NAME);
            if (data == null)
                data = new Integer[0];
            startSimulation();
            simulator.rate = rate.length > 0 ? rate[0] : simulator.rate;
            initialized = true;
            simulator = this;
        }
        return simulator;
    }

    public static BreathSimulator getBreathSimulator() {
        return simulator;
    }

    public static boolean recordData(int size) {
        if (!AppState.simulateBreathy) {
            DataCollector dataCollector = new DataCollector(size);
            BreathData.addObserver(dataCollector);
        } else {
            if (AppState.btState != AppState.BtState.Connected)
                Log.e(TAG, "Please connect to a Breathy device");
            else
                Log.e(TAG, "Can not record the Breathy data. Please disable simulator at AppState!");
        }
        return false;
    }


    private static class DataCollector implements IObserver {
        private Integer[] data;
        private int index = 0;

        public DataCollector(int size) {
            data = new Integer[size];
            simulator.recording = true;
            AppState.recordData = true;
        }

        @Override
        public void call(Object... messages) {
            if (index < data.length) {
                data[index++] = (Integer) messages[0];
            } else {
                BreathData.removeObserver(this);
                AppState.recordData = true;
                simulator.saveData.writeObject(DATA_NAME, data);
                simulator.recording = false;
                Log.d(TAG, "Data collected!");
            }
        }
    }

    public void removeRecordings(boolean forever) {
        if (forever && initialized)
            saveData.writeObject(DATA_NAME, null);
        data = new Integer[0];

    }

    public boolean connect() {
        if (initialized && AppState.btState != AppState.BtState.Connected)
            startSimulation();
        return AppState.btState == AppState.BtState.Connected;
    }

    public boolean disconnect() {
        if (initialized) {
            AppState.btState = AppState.BtState.Enabled;
            AppState.recordData = false;
        }
        return initialized;
    }

    private void startSimulation() {
        AppState.btState = AppState.BtState.Connected;

        Thread simulator = new Thread(null, new Runnable() {
            private long start = System.currentTimeMillis();
            private int value = 0;
            @Override
            public void run() {
                Looper.prepare();
                int index = 0;
                long delta = 0;
                double millis = 1000 / rate;
                while (AppState.btState == AppState.BtState.Connected) {
                    delta = System.currentTimeMillis() - start;
                    if(data == null || data.length==0 || index>=data.length){
                        generate();
                        index = 0;
                    }
                    if (!recording && delta >= millis) {
                        start = System.currentTimeMillis();
                        BreathData.add(new BreathData.Element(data[index++]));
                        index = index % data.length;
                        index++;
                    }
                }
            }
        }, "Breath Data Simulator");
        simulator.setPriority(Thread.MIN_PRIORITY);
        simulator.start();
    }

    private Random r = new Random(0);
    private void generate(){
        int kind = r.nextInt(3)%3;
        breath();
//        if(kind==0)
//            breath();
//        else if(kind==1)
//            breathIn();
//        else breathOut();
    }

    private void breath() {
        data = new Integer[20];
        data[0] = 700;
        data[1] = 750 + r.nextInt()%30;
        data[2] = 800 + r.nextInt()%30;
        data[3] = 900 + r.nextInt()%30;
        data[4] = 1000 + r.nextInt()%30;
        data[5] = 950 + r.nextInt()%30;
        data[6] = 850 + r.nextInt()%30;
        data[7] = 750 + r.nextInt()%30;
        data[8] = 700 + r.nextInt()%30;
        data[9] = 700 + r.nextInt()%30;
        data[10] = 650 + r.nextInt()%30;
        data[11] = 600 + r.nextInt()%30;
        data[12] = 550 + r.nextInt()%30;
        data[13] = 500 + r.nextInt()%30;
        data[14] = 450 + r.nextInt()%30;
        data[15] = 400 + r.nextInt()%30;
        data[16] = 300 + r.nextInt()%30;
        data[17] = 400 + r.nextInt()%30;
        data[18] = 550 + r.nextInt()%30;
        data[19] = 700 + r.nextInt()%30;
    }
    private void pause() {
        data = new Integer[10];
        data[0] = 700 + r.nextInt()%10;
        data[1] = 700 + r.nextInt()%10;
        data[2] = 700 + r.nextInt()%10;
        data[3] = 700 + r.nextInt()%10;
        data[4] = 700 + r.nextInt()%10;
        data[5] = 700 + r.nextInt()%10;
        data[6] = 700 + r.nextInt()%10;
        data[7] = 700 + r.nextInt()%10;
        data[8] = 700 + r.nextInt()%10;
        data[9] = 700 + r.nextInt()%10;
    }
    private void breathIn() {
        data = new Integer[10];
        data[0] = 700;
        data[1] = 750 + r.nextInt()%30;
        data[2] = 800 + r.nextInt()%30;
        data[3] = 900 + r.nextInt()%30;
        data[4] = 1000 + r.nextInt()%24;
        data[5] = 950 + r.nextInt()%30;
        data[6] = 850 + r.nextInt()%30;
        data[7] = 750 + r.nextInt()%30;
        data[8] = 700 + r.nextInt()%30;
        data[9] = 700 + r.nextInt()%30;
    }
    private void breathOut() {
        data = new Integer[8];
        data[0] = 700;
        data[1] = 600 + r.nextInt()%30;
        data[2] = 500 + r.nextInt()%30;
        data[3] = 400 + r.nextInt()%30;
        data[4] = 300 + r.nextInt()%30;
        data[5] = 400 + r.nextInt()%30;
        data[6] = 550 + r.nextInt()%30;
        data[7] = 700 + r.nextInt()%30;
    }
}
