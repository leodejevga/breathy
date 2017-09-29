package com.apps.philipps.app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.apps.philipps.app.simulator.BreathSimulator;
import com.apps.philipps.audiosurf.AudioSurf;
import com.apps.philipps.fade.Fade;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.Coins;
import com.apps.philipps.source.SaveData;
import com.apps.philipps.source.cachemanager.CacheManager;
import com.apps.philipps.source.interfaces.IGame;
import com.apps.philipps.test.Test;
import com.breathy.racing.Race;

import java.util.ArrayList;
import java.util.List;

import breathing.philipps.apps.com.breathing.BreathingGame;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */

/**
 * The type Backend.
 */
public class Backend {
    /**
     * Games that are loaded
     */
    public static List<IGame> games;
    /**
     * Selected Game
     */
    public static IGame selected;
    public static boolean choosen;
    private static boolean initialized = false;
    private static BluetoothService bluetoothService = null;
    private static BluetoothAdapter adapter;
    private static BreathSimulator breathSimulator;
    public static CacheManager cacheManager;

    /**
     * Reinitialize the hole Backend Data
     *
     * @param context from the Main Activity
     */
    public static void reinit(Context context) {
        initialized = false;
        init(context);
    }

    /**
     * Initialize Backend if not initialized
     *
     * @param context from the Main Activity
     * @return true if has initialized
     */
    public static boolean init(Context context) {
        if (!initialized) {
            SaveData.loadPlanManager();
            cacheManager = new CacheManager(context);
            games = new ArrayList<>();
            Backend.games.add(new AudioSurf());
            Backend.games.add(new Test());
            Backend.games.add(new Race());
            Backend.games.add(new Fade());
            Backend.games.add(new BreathingGame());
            Backend.games.add(new com.apps.philipps.opengltest.Test());


            for(IGame game : Backend.games){
                game.init(context, Backend.cacheManager.isIGameBought(game.getName()));
            }
            BreathData.init(context);
            Coins.init(context);
            if(AppState.simulateBreathy) {
                breathSimulator = BreathSimulator.getBreathSimulator();
                breathSimulator.init(context, AppState.breathyDataFrequency);
//              breathSimulator.removeRecordings(true);
//              BreathSimulator.recordData(500);
            }
            initialized = true;
            return true;
        }
        return false;
    }

//    public static boolean bluetoothEnabled(){
//        if (adapter!=null)
//            return adapter.isEnabled();
//        return false;
//    }
//    public static boolean bluetoothConnected(){
//        return bluetoothService!=null && bluetoothService.getState() == BluetoothService.STATE_CONNECTED;
//    }

    public static void startBTService() {
        if (AppState.simulateBreathy)
            breathSimulator.connect();
        else if (bluetoothService == null)
            bluetoothService = new BluetoothService();
    }

    public static void destroy() {
        if (bluetoothService != null)
            bluetoothService.stop();
    }

    public static void bluetoothResume() {
        if (AppState.simulateBreathy) {
            breathSimulator.connect();
        } else if (bluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (bluetoothService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                bluetoothService.start();
            }
        }
    }

    public static void connectDevice(BluetoothDevice device, boolean secure) {
        if (AppState.simulateBreathy) {
            breathSimulator.connect();
        } else {
            startBTService();
            bluetoothService.connect(device, secure);
        }
    }
}
