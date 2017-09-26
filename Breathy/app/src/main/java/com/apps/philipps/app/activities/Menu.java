package com.apps.philipps.app.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apps.philipps.app.Backend;
import com.apps.philipps.app.BluetoothService;
import com.apps.philipps.app.R;
import com.apps.philipps.source.AppState;

/**
 * Main Activity.
 */
public class Menu extends Activity {
    MediaPlayer myMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter enabled = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter connected = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter disconnected = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(AppState.btStateChanger, enabled);
        registerReceiver(AppState.btStateChanger, connected);
        registerReceiver(AppState.btStateChanger, disconnected);
        AppState.initBtState();
        Backend.init(this);
        initActivity();
    }

    private void checkBluetooth() {
        if(AppState.btState == AppState.BtState.Enabled && !Backend.choosen) {
            Backend.choosen = true;
            Intent i = new Intent(this, Devices.class);
            startActivityForResult(i, BluetoothService.REQUEST_CONNECT_DEVICE_SECURE);
        }
    }

    private void initActivity() {
        Button games = (Button) findViewById(R.id.mainGames);
        games.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this, SelectGame.class);
            startActivity(i);
        });
        Button options = (Button) findViewById(R.id.mainOptions);
        options.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this, Options.class);
            startActivity(i);
        });
        Button breathPlan = (Button) findViewById(R.id.mainBreathPlan);
        breathPlan.setOnClickListener(v -> {
            Intent i = new Intent(Menu.this, PlansManager.class);
            startActivity(i);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AppState.btState == AppState.BtState.Disabled && !AppState.btAsked) {
            AppState.btAsked=true;
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, BluetoothService.REQUEST_ENABLE_BT);
        }
        else
            Backend.startBTService();
    }

    @Override
    public void onPause() {
        super.onPause();
        myMediaPlayer.release();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(AppState.btStateChanger);
        Backend.destroy();
    }
    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        Backend.bluetoothResume();
        checkBluetooth();
        myMediaPlayer = MediaPlayer.create(this, R.raw.gamemenu);
        myMediaPlayer.setLooping(true);
        myMediaPlayer.start();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BluetoothService.REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case BluetoothService.REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case BluetoothService.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(BluetoothDevice.EXTRA_DEVICE);
        if (address != null) {
            // Get the BluetoothDevice object
            BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
            // Attempt to connect to the device
            Backend.connectDevice(device, secure);
        }
    }

    public void createPlan(View view) {
        Intent i = new Intent(this, CreatePlanPart.class);
        startActivity(i);
    }

    public void calibrate(View view) {
        Intent i = new Intent(this, Calibrate.class);
        startActivity(i);
    }
}
