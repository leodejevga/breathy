package com.apps.philipps.app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.apps.philipps.audiosurf.AudioSurf;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.interfaces.IGame;

import java.util.ArrayList;
import java.util.List;

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
    private static boolean initialized=false;
    private static BluetoothService bluetoothService = null;
    //TODO: Hier kommen weitere Daten hin die von überall zugreifbar sein müssen. In der Methode init werden sie initialisiert

    /**
     * Reinitialize the hole Backend Data
     *
     * @param context from the Main Activity
     */
    public static void reinit(Context context){
        initialized = false;
        init(context);
    }

    /**
     * Initialize Backend if not initialized
     *
     * @param context from the Main Activity
     * @return true if has initialized
     */
    public static boolean init(Context context){
        if(!initialized){
            games = new ArrayList<>();
            Backend.games.add(new AudioSurf(context)); //TODO: Automatisches Füllen der Spiele in die Liste
            bluetoothService = new BluetoothService();
            BreathData.init(0);
            //TODO: Initialisieren von weiteren Objekten, die diese Klasse haben wird
            initialized = true;
            return true;
        }
        return false;
    }

    public static boolean bluetoothEnabled(){
        return bluetoothService.isEnabled();
    }
    public static boolean bluetoothConnected(){
        return bluetoothService.getState() != BluetoothService.STATE_NONE;
    }

    public static void destroy() {
        bluetoothService.stop();
    }

    public static void bluetoothResume() {
        if (bluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (bluetoothService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                bluetoothService.start();
            }
        }
    }

    public static BluetoothAdapter getAdapter() {
        return bluetoothService.getAdapter();
    }

    public static void connectDevice(BluetoothDevice device, boolean secure) {
        bluetoothService.connect(device, secure);
    }
}
