package com.apps.philipps.source;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;

/**
 * Created by Jevgenij Huebert on 22.02.2017. Project Breathy
 */
public class AppState {
    public final static String PLAN_STORAGE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "PlanManager.pm";
    public final static String DATA_STORAGE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "BreathData" + File.separator;
    public final static boolean simulateBreathy = true; //Debug purpose
    public final static boolean DEVELOPER = true;


    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean inGame = false;
    public static boolean recordData = false;
    public static boolean btAsked = false;

    public static AppState.BtState btState = BtState.Disabled;

    public final static double MAX_BT_VALUE = 1024;
    public static int breathyNormState = 700;
    public static int breathyDataFrequency = 4;
    public static double breathyUserMax = 1024;
    public static double breathyUserMin = 200;

    public final static BroadcastReceiver btStateChanger = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            btState = BtState.Disabled;
                            break;
                        case BluetoothAdapter.STATE_ON:
                            btState = BtState.Enabled;
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    btState = BtState.Connected;
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    btState = BtState.Enabled;
                    break;
            }
            Log.w("AppState Bluetooth", AppState.btState + "");
        }
    };

    public static void initBtState() {
        BluetoothAdapter a = BluetoothAdapter.getDefaultAdapter();
        if (a == null)
            btState = BtState.None;
        else if (a.isEnabled())
            btState = BtState.Enabled;
        else
            btState = BtState.Disabled;
    }

    public enum BtState {
        None,
        Disabled,
        Enabled,
        Connected
    }


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }
        return true;
    }
}
