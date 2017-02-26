package com.apps.philipps.app.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.apps.philipps.app.Backend;
import com.apps.philipps.app.BluetoothService;
import com.apps.philipps.app.R;
import com.apps.philipps.audiosurf.AudioSurf;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.Coins;

/**
 * Main Activity.
 */
public class Menu extends Activity {

    private Button games;
    private Button options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backend.init(this);
        initActivity();
    }

    private void checkBluetooth() {
        if(Backend.bluetoothEnabled() && !Backend.bluetoothConnected() && !Backend.choosen) {
            Backend.choosen = true;
            Intent i = new Intent(this, Devices.class);
            startActivityForResult(i, BluetoothService.REQUEST_CONNECT_DEVICE_SECURE);
        }
    }

    private void initActivity() {
        games = (Button) findViewById(R.id.mainGames);
        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, SelectGame.class);
                startActivity(i);
            }
        });
        options = (Button) findViewById(R.id.mainOptions);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Options.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!Backend.bluetoothEnabled() && !AppState.btAsked) {
            AppState.btAsked=true;
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, BluetoothService.REQUEST_ENABLE_BT);
        }
        else
            Backend.startBTService();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
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
            BluetoothDevice device = Backend.getAdapter().getRemoteDevice(address);
            // Attempt to connect to the device
            Backend.connectDevice(device, secure);
        }
    }
}
