package com.apps.philipps.source;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

/**
 * Created by Jevgenij Huebert on 22.02.2017. Project Breathy
 */
public class AppState {
    public final static String PLAN_STORAGE = Environment.getExternalStorageDirectory() + "/Download/PlanManager.pm";
    public final static boolean simulateBreathy = true; //Debug purpose

    public static boolean inGame = false;
    public static boolean recordData = false;
    public static boolean btAsked=false;

    public static AppState.BtState btState = BtState.Disabled;
    public static FrameLimit framelimit = FrameLimit.Unlimited;

    public static int breathyNormState = 700;
    public static int breathyDataFrequency = 3;
    public static int breathyUserMax = 1024;
    public static int breathyUserMin = 200;

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
        if(a==null)
            btState = BtState.None;
        else if(a.isEnabled())
            btState = BtState.Enabled;
        else
            btState = BtState.Disabled;
    }

    public enum BtState{
        None,
        Disabled,
        Enabled,
        Connected
    }

    public enum FrameLimit{
        Movie(24),
        Thirty(30),
        Sixty(60),
        HundredTwenty(120),
        Unlimited(1000, "Unlimited");

        int frameLimit;
        String[] data;
        FrameLimit(int frames, String... data){
            frameLimit = frames;
        }
        public int getLimit(){
            return frameLimit;
        }
        public String getData(){
            return data.length>0?data[0]:"limit: " + framelimit;
        }
    }
}
