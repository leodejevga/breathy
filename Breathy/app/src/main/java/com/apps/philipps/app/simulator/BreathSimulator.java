package com.apps.philipps.app.simulator;

import android.content.Context;
import android.provider.Settings;
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
                data = data;
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

        new Thread(null, new Runnable() {
            private long start = System.currentTimeMillis();

            @Override
            public void run() {

                int index = 0;
                long delta = 0;
                double millis = 1000 / rate;
                while (AppState.btState == AppState.BtState.Connected) {
                    delta = System.currentTimeMillis() - start;
                    if(data == null || data.length==0 || index>=data.length){
                        generateData();
                        index = 0;
                    }
                    if (!recording && delta >= millis) {
                        start = System.currentTimeMillis();
                        BreathData.add(data[index]);
                        index++;
                        index = index % data.length;
                        index++;
                    }
                }
            }
        }, "Breath Data Simulator").start();
    }


    private void generateData() {
        Random r = new Random(0);
        data = new Integer[20];
        data[0] = 700 + r.nextInt(5);
        data[1] = 750 + r.nextInt(5);
        data[2] = 800 + r.nextInt(5);
        data[3] = 900 + r.nextInt(5);
        data[4] = 1000 + r.nextInt(5);
        data[5] = 950 + r.nextInt(5);
        data[6] = 850 + r.nextInt(5);
        data[7] = 750 + r.nextInt(5);
        data[8] = 700 + r.nextInt(5);
        data[9] = 700 + r.nextInt(5);
        data[10] = 650 + r.nextInt(5);
        data[11] = 600 + r.nextInt(5);
        data[12] = 550 + r.nextInt(5);
        data[13] = 500 + r.nextInt(5);
        data[14] = 450 + r.nextInt(5);
        data[15] = 400 + r.nextInt(5);
        data[16] = 350 + r.nextInt(5);
        data[17] = 400 + r.nextInt(5);
        data[18] = 550 + r.nextInt(5);
        data[19] = 700 + r.nextInt(5);
    }
}
